(ns primeseeker.ui.view
  (:require [hiccup2.core :as h]
            ;; [com.reasonr.scriptjure :refer [js]]
            ))

(defn status [req]
  (h/html
      (h/raw "<!DOCTYPE html>")
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name :viewport :content "width=device-width, initial-scale=1"}]
      [:title "Primeseeker"]
      [:meta {:name    :description
              :content "Prime number database"}]
      [:meta {:name    :author
              :content "Giorgos Papadokostakis"}]
      [:script {:src "https://cdn.tailwindcss.com"}]]
     [:body
      [:h1 "Primeseeker"]
      [:div
       [:button.border-solid.border-black.border.rounded.p-2.bg-blue-300
        ;; {:onclick (js (alert "Hello Mitsos!"))}
        "Work!"]]]]))
