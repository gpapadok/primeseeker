(ns primeseeker.api
  (:require [primeseeker.primes :as primes]))

(defn index
  [request]
  {:status 200
   :body   {:message "Welcome to PrimeSeeker API!"}})

(defn allocate-number
  [{datasource :datasource parameters :parameters :as request}]
  (if-let [n (primes/allocate-number-to-worker! datasource)]
    {:status 200
     :body (update n :number (if (-> parameters :query :asstring) str identity))}
    {:status 404}))

(defn update-number-status
  [{datasource :datasource parameters :parameters :as request}]
  (let [n        (get-in parameters [:body :number])
        is-prime (get-in parameters [:body :prime?])
        proc-id  (get-in parameters [:body :proc-id])
        coerced-n  (if (string? n) (Integer/parseInt n) n)]
    (if (primes/matching-number-proc-id coerced-n proc-id)
      (do
        (primes/update-number! datasource coerced-n is-prime)
        {:status 200
         :body   {:message "Update successful"}})
      {:status 400
       :body   {:message "Invalid id for number"}})))

(defn get-primes-db
  [{datasource :datasource :as request}]
  {:status 200
   :body   (primes/get-all-numbers datasource)})
