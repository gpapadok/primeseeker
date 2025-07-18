(ns primeseeker.ui.routes
  (:require [primeseeker.ui.handlers :as ui.handlers]
            [primeseeker.spec :as s]))

(def routes
  [["/primes" {:get {:name       :get-primes
                     :parameters {:query ::s/pagination}
                     :handler    ui.handlers/primes}}]
   ["/work" {:get {:name    :work
                   :handler ui.handlers/work-view}}]])
