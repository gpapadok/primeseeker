(ns primeseeker.api
  (:require [primeseeker.primes :as primes]))

(defn index
  [request]
  {:status 200
   :body   {:message "Welcome to PrimeSeeker API!"}})

(defn allocate-number
  [{datasource :datasource :as request}]
  (if-let [n (primes/allocate-number-to-worker! datasource)]
    {:status 200
     :body   n}
    {:status 404}))

(defn update-number-status
  [{datasource :datasource :as request}]
  (let [n        (get-in request [:body-params :number])
        is-prime (get-in request [:body-params :prime?])
        proc-id  (get-in request [:body-params :proc-id])]
    (if (primes/matching-number-proc-id n proc-id)
      (do
        (primes/update-number! datasource n is-prime)
        {:status 200
         :body   {:message "Update successful"}})
      {:status 400
       :body   {:message "Invalid id for number"}})))

(defn get-primes-db
  [{datasource :datasource :as request}]
  {:status 200
   :body   (primes/get-all-numbers datasource)})
