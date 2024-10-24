(ns primeseeker.primes
  (:require [next.jdbc :as jdbc])
  (:require [next.jdbc.result-set :as rs]))

(def db {:dbtype "sqlite" :dbname "primes"})

(def ds (delay (jdbc/get-datasource db)))

(def primes-db (atom {}))

(defn- ds-execute! [& query]
  (jdbc/execute! @ds query {:builder-fn rs/as-unqualified-kebab-maps}))

(defn index-db [n]
  (ds-execute! "select * from prime where number = ?" n))

(defn get-all-numbers []
  (ds-execute! "select * from prime"))

(defn- add-number! [p]
  (ds-execute! "insert into prime (number, is_prime) values (?, null)" p))

(defn all-numbers []
  (mapv :number (ds-execute! "select number from prime")))

(defn create-and-add-number! []
  (let [numbers     (all-numbers)
        max-n       (apply max numbers)
        next-number (+ max-n 2)]
    (add-number! next-number)
    next-number))

(defn first-available []
  (let [unprocessed (->> (ds-execute! "select number from prime where is_prime is null")
                         (mapv :number)
                         set)
        processing  (->> @primes-db
                         (filter (fn [[n m]] (:processing m)))
                         (map first)
                         set)
        available   (clojure.set/difference unprocessed
                                            processing)]
    (and (seq available) (apply min available))))

(defn get-first-available []
  (or (first-available) (create-and-add-number!)))

(defn matching-number-proc-id [number proc-id]
  (let [db-entry (@primes-db number)]
    (and (:processing db-entry) (= proc-id (:proc-id db-entry)))))

(defn allocate-number-to-worker! []
  (let [uuid        (java.util.UUID/randomUUID)
        next-number (get-first-available)]
    (swap! primes-db
           (fn [primes-col n]
             (assoc primes-col n {:processing true
                                  :proc-id    uuid}))
           next-number)
    {:proc-id uuid
     :number  next-number}))

(defn update-number! [n is-prime]
  (ds-execute! "update prime set is_prime = ? where number = ?" is-prime n)
  (swap! primes-db #(dissoc % n)))
