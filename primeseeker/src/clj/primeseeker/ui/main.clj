(ns primeseeker.ui.main
  (:require [primeseeker.ui.navbar :refer [navbar]]
            [hiccup2.core :as h]))

(defn main [body]
  (h/html (h/raw "<!DOCTYPE html>")
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name :viewport :content "width=device-width, initial-scale=1"}]
      [:title "Primeseeker"]
      [:meta {:name    :description
              :content "Prime number database"}]
      [:meta {:name    :author
              :content "Giorgos Papadokostakis"}]
      [:script {:src "https://cdn.tailwindcss.com"}] ; TODO: Remove tailwind from cdn
      [:script {:src "js/main.js"}]
      ]
     [:body.m-10.bg-stone-100
      [:div.flex
       (navbar)
       [:div.flex body]]]]))
