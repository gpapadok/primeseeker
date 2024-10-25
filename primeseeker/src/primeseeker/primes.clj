(ns primeseeker.primes
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [taoensso.telemere :as t]))

;;; DB ;;;
(def db {:dbtype "sqlite" :dbname "primes"})

(defn create-datasource []
  (delay (jdbc/get-datasource db)))

(defn- ds-execute! [ds & query]
  (jdbc/execute! @ds query {:builder-fn rs/as-unqualified-kebab-maps}))
;;;;;;;;;;

;;; CACHE ;;;
(defprotocol PrimesCache
  "Cache to store numbers being processed to evaluate primality"
  (insert! [this number uuid] "Insert number")
  (delete! [this number] "Delete number")
  (inspect [this] "Get all"))

(defn- create-atom-cache []
  (let [cache (atom {})]
    (reify PrimesCache
      (insert! [this number uuid]
        (swap! cache
               #(assoc % number {:processing true
                                 :proc-id    uuid})))
      (delete! [this number] (swap! cache #(dissoc % number)))
      (inspect [this] @cache))))

;; NOTE: Method calls on this only work with dot operator for some reason
(def ^:dynamic *cache* (create-atom-cache))

(defn- cache-insert! [number uuid]
  (t/log! :info ["Cache - number cached (v2):" number])
  (. *cache* insert! number uuid))

(defn- cache-delete! [number]
  (t/log! :info ["Cache - number deleted (v2):" number])
  (. *cache* delete! number))

(defn- cache-all-processing []
  (->> (. *cache* inspect)
       (filter (fn [[n m]] (:processing m)))
       (map first)
       set))

(defn- cache-get [n]
  ((. *cache* inspect) n))
;;;;;;;;;;;;;;

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
        processing  (cache-all-processing)
        available   (clojure.set/difference unprocessed
                                            processing)]
    (and (seq available) (apply min available))))

(defn- get-first-available [ds]
  (or (first-available ds) (create-and-add-number! ds)))

(defn matching-number-proc-id [number proc-id]
  (let [db-entry (cache-get number)]
    (and (:processing db-entry) (= proc-id (:proc-id db-entry)))))

(defn allocate-number-to-worker! [ds]
  (let [uuid        (java.util.UUID/randomUUID)
        next-number (get-first-available ds)]
    (cache-insert! next-number uuid)
    {:proc-id uuid
     :number  next-number}))

(defn update-number! [ds n is-prime]
  (ds-execute! ds "update prime set is_prime = ? where number = ?" is-prime n)
  (cache-delete! n))
