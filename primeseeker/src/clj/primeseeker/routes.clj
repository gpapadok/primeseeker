(ns primeseeker.routes
  (:require [muuntaja.middleware]
            [reitit.ring]
            [reitit.ring.coercion]
            [reitit.coercion.spec]
            [reitit.coercion]
            [ring.middleware.params]
            [ring.middleware.keyword-params]
            [primeseeker.api :as api]
            [primeseeker.handlers.view :as handlers.view]
            [primeseeker.primes :refer [create-datasource]]))

(def routes
  [["/primes" {:get {:name       :get-primes
                     :coercion   reitit.coercion.spec/coercion
                     :parameters {:query {:limit int? :offset int?}}
                     :handler    handlers.view/primes}}]
   ["/api"
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

(defn wrap-local-cors [handler]
  (fn [request]
    (let [origin   (-> request :headers (get "origin"))
          response (handler request)]
      (if (and origin (re-matches #"^http://localhost:\d+" origin))
        (update response
                :headers (fn [headers]
                           (assoc headers "Access-Control-Allow-Origin" origin)))
        response))))

(def handler
  (reitit.ring/ring-handler
   (reitit.ring/router
    routes
    {:data {:compile    reitit.coercion/compile-request-coercers
            :coercion   reitit.coercion.spec/coercion
            :middleware [reitit.ring.coercion/coerce-request-middleware]}})
   (constantly {:status 404 :body {:message "Not found"}})
   {:middleware
    [muuntaja.middleware/wrap-format
     wrap-datasource
     wrap-local-cors
     ring.middleware.params/wrap-params
     ring.middleware.keyword-params/wrap-keyword-params
     reitit.ring.coercion/coerce-request-middleware]}))
