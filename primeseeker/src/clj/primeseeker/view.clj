(ns primeseeker.view
  (:require [primeseeker.ui.primes :as ui.primes]
            [primeseeker.primes :as store]))

(defn primes [{:keys [datasource] :as req}]
  (ui.primes/primes (store/get-primes datasource)))
