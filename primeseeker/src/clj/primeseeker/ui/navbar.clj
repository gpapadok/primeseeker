(ns primeseeker.ui.navbar
  (:require [hiccup2.core :as h]))

(defn navbar []
  [:navbar
   [:a {:href "#"} "Primes"]
   [:a {:href "#"} "In Testing"]
   [:a {:href "#"} "Work"]])
