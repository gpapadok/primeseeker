(ns primeseeker.primes
  (:require [next.jdbc :as jdbc])
  (:require [next.jdbc.result-set :as rs]))

(def db {:dbtype "sqlite" :dbname "primes"})

(def primes-db (atom {}))

(defn create-datasource []
  (delay (jdbc/get-datasource db)))

(defn- ds-execute! [ds & query]
  (jdbc/execute! @ds query {:builder-fn rs/as-unqualified-kebab-maps}))

(defn- index-db [ds n]
  (ds-execute! ds "select * from prime where number = ?" n))

(defn get-all-numbers [ds]
  (ds-execute! ds "select * from prime"))

(defn- add-number! [ds p]
  (ds-execute! ds "insert into prime (number, is_prime) values (?, null)" p))

(defn- all-numbers [ds]
  (mapv :number (ds-execute! ds "select number from prime")))

(defn- create-and-add-number! [ds]
  (let [numbers     (all-numbers ds)
        max-n       (apply max numbers)
        next-number (+ max-n 2)]
    (add-number! ds next-number)
    next-number))

(defn- first-available [ds]
  (let [unprocessed (->> (ds-execute! ds "select number from prime where is_prime is null")
                         (mapv :number)
                         set)
        processing  (->> @primes-db
                         (filter (fn [[n m]] (:processing m)))
                         (map first)
                         set)
        available   (clojure.set/difference unprocessed
                                            processing)]
    (and (seq available) (apply min available))))

(defn- get-first-available [ds]
  (or (first-available ds) (create-and-add-number! ds)))

(defn matching-number-proc-id [number proc-id]
  (let [db-entry (@primes-db number)]
    (and (:processing db-entry) (= proc-id (:proc-id db-entry)))))

(defn allocate-number-to-worker! [ds]
  (let [uuid        (java.util.UUID/randomUUID)
        next-number (get-first-available ds)]
    (swap! primes-db
           (fn [primes-col n]
             (assoc primes-col n {:processing true
                                  :proc-id    uuid}))
           next-number)
    {:proc-id uuid
     :number  next-number}))

(defn update-number! [ds n is-prime]
  (ds-execute! ds "update prime set is_prime = ? where number = ?" is-prime n)
  (swap! primes-db #(dissoc % n)))
