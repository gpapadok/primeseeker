(ns primeseeker.ui.work
  (:require [hiccup2.core :as h]
            [primeseeker.ui.main :refer [main]]))

(defn work-button []
  [:div.flex.mx-20
   [:button.rounded-xl.border-slate-100.border-2.p-3.bg-indigo-500.m-auto
    {:class "hover:bg-stone-100 hover:border-indigo-500"
     :onclick "(() => alert('hello'))()"}
    "Work"]])

(defn work-view []
  (main (work-button)))
