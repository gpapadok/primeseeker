(ns primeseeker.ui.work
  (:require [primeseeker.ui.main :refer [main]]
            [hiccup2.core :as h]))

(defn- work-button []
  [:div.flex.mx-20
   [:button.rounded-xl.border-slate-100.border-2.p-3.bg-indigo-500.m-auto
    {:class "hover:bg-stone-100 hover:border-indigo-500"
     :onclick "primeseeker.view.work()"}
    "Work"]])

(defn- pause-button []
  [:div.flex.mx-20
   [:button.rounded-xl.border-slate-100.border-2.p-3.bg-indigo-500.m-auto
    {:class "hover:bg-stone-100 hover:border-indigo-500"
     :onclick "primeseeker.view.pause()"}
    "Pause"]])

(defn- component []
  [:div
   (work-button)
   (pause-button)])

(defn work []
  (main (component)))
