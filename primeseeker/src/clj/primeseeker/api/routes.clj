(ns primeseeker.api.routes
  (:require [primeseeker.api.handlers :as api]
            [primeseeker.spec :as s]))

(def routes
  [["/api"
    ["" {:get api/index}]
    ["/primes" {:get {:name    :get-primes-db
                      :parameters {:query ::s/pagination}
                      :handler api/get-primes-db}}]
    ["/work" {:get  {:name       :allocate-number
                     :parameters {:query ::s/work-query}
                     :responses  {200 {:body {:number ::s/number
                                              :proc-id ::s/proc-id}}}
                     :handler    api/allocate-number}
              :post {:name       :update-number
                     :parameters {:body {:number  ::s/number
                                         :proc-id ::s/proc-id
                                         :prime?  ::s/prime?}}
                     :handler    api/update-number-status}}]]])
