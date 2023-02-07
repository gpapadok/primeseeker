(ns primeseeker.routes
  (:require [primeseeker.api :as api]
            [reitit.ring :as reitit]))

(def routes
  [["/" {:get api/index}]
   ["/primes" {:get {:name    :get-primes-db
                     :handler api/get-primes-db}}]
   ["/work" {:get  {:name    :allocate-number
                    :handler api/allocate-number}
             :post {:name       :update-number
                    :parameters {:body-params {:number nat-int?
                                               :prime? boolean?}}
                    :handler    api/update-number-status}}]])

(def handler
  (reitit/ring-handler
   (reitit/router routes)))
