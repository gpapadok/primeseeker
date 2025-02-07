(ns primeseeker.handlers.view
  (:require [primeseeker.ui.primes :as ui.primes]
            [primeseeker.primes :as store]))

(defn primes [{:keys [params datasource] :as req}]
  {:body (str (ui.primes/primes
               (store/get-primes datasource params)))
   :status 200})
