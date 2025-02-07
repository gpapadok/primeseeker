(ns primeseeker.ui.primes
  (:require [primeseeker.ui.main :refer [main]]
            [hiccup2.core :as h]))

(defn primes [primes] ;; [{:num 10 :processed-at timestamp}]
  (main
   [:table
    [:thead
     [:tr
      [:th "Prime"]
      [:th "Processed at"]]]
    (cons :tbody
          (mapv (fn [p]
                  [:tr
                   [:td (str (:num p))]
                   [:td (:processed-at p)]])
                primes))]))
