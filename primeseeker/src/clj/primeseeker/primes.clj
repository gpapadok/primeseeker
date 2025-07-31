(ns primeseeker.primes
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [taoensso.telemere :as t]
            [primeseeker.store :as store]
            [primeseeker.cache :as cache]))

(def create-datasource store/create-sqlite-datasource)

;; NOTE: Method calls on this only work with dot operator for some reason
(def ^:dynamic *cache* (cache/create-atom-cache))

(defn- cache-insert! [number uuid]
  ;; (t/log! :info ["Cache - number cached (v2):" number])
  (. *cache* insert! number uuid))

(defn- cache-delete! [number]
  ;; (t/log! :info ["Cache - number deleted (v2):" number])
  (. *cache* delete! number))

(defn- cache-all-processing []
  (->> (. *cache* inspect)
       (filter (fn [[n m]] (:processing m)))
       (map first)
       set))

(defn- cache-get [n]
  ((. *cache* inspect) n))

(defn get-all-numbers [ds {:keys [limit offset]
                           :or {limit 20 offset 0}
                           :as pagination}]]
  (store/get-all-numbers ds))

(defn get-primes [ds & {:keys [limit offset]
                        :or {limit 20 offset 0}
                        :as pagination}]
  (store/get-primes ds limit offset))

(defn count-primes [ds]
  (-> (store/count-primes ds)
      first
      vals
      first))

(defn- create-and-add-number! [ds]
  (let [numbers     (store/all-numbers ds)
        max-n       (if (empty? numbers) 1 (apply max numbers))
        next-number (+ max-n 2)]
    (store/add-number! ds next-number)
    next-number))

(defn- first-available [ds]
  (let [unprocessed (->> (store/get-untested-numbers ds)
                         (mapv :num)
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
  (let [uuid        (random-uuid)
        next-number (get-first-available ds)]
    (cache-insert! next-number uuid)
    {:proc-id uuid
     :number  next-number}))

(defn update-number! [ds n is-prime]
  (store/update-tested-number! ds n is-prime)
  (cache-delete! n))
