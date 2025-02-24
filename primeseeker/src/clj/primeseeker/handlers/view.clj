(ns primeseeker.handlers.view
  (:require [primeseeker.ui.primes :as ui.primes]
            [primeseeker.primes :as store]))

(defn primes [{:keys [params datasource] :as req}]
  (let [n-primes (store/count-primes datasource)
        primes   (store/get-primes datasource params)]
    {:body   (str (ui.primes/primes ; TODO: `str` could be in mw
                   primes
                   (assoc params :count n-primes)))
     :status 200}))
