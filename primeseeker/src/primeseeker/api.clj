(ns primeseeker.api
  (:require [primeseeker.primes :as primes]))


(defn index
  [request]
  {:status 200
   :body   {:message "Welcome to PrimeSeeker API!"}})

(defn allocate-number
  [request]
  (if-let [n (primes/allocate-number-to-worker!)]
    {:status 200
     :body   {:number n}}
    {:status 404}))

;; FIX THIS
(defn update-number-status
  [request]
  (let [n        (get-in request [:body-params :number])
        is-prime (get-in request [:body-params :prime?])]
    ;; (prn 'mitsos n is-prime)
    (prn 'reset)
    (primes/update-number! n is-prime)
    {:status 200
     :body   {:message "Update successful"}}))

(defn get-primes-db
  [request]
  (if-let [primes-db @primes/primes-db]
    {:status 200
     :body   @primes/primes-db}
    {:status 404}))
