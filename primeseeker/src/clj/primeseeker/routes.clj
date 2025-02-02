(ns primeseeker.routes
  (:require [primeseeker.api :as api]
            [primeseeker.primes :refer [create-datasource]]
            [reitit.ring :as reitit]))

(def routes
  [["/api"
    ["" {:get api/index}]
    ["/primes" {:get {:name    :get-primes-db
                      :handler api/get-primes-db}}]
    ["/work" {:get  {:name    :allocate-number
                     :handler api/allocate-number}
              :post {:name       :update-number
                     :parameters {:body-params {:number nat-int?
                                                :prime? boolean?}}
                     :handler    api/update-number-status}}]]])

(defn wrap-datasource [handler]
  (fn [request]
    (handler (assoc request :datasource (create-datasource)))))

(def handler
  (reitit/ring-handler
   (reitit/router routes)
   (constantly {:status 404 :body {:message "Not found"}})
   {:middleware [wrap-datasource]}))
