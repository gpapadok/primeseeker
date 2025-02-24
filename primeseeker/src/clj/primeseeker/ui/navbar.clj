(ns primeseeker.ui.navbar
  (:require [hiccup2.core :as h]))

(defn navbar []
  [:navbar.bg-zinc-200.rounded-lg.m-3
   [:div.text-zinc-800.m-5
    [:div.m-3.p-1
     [:a.p-2.rounded-xl {:class "hover:bg-stone-400 hover:text-stone-300"
                         :href  "#"}
      "Primes"]]
    [:div.m-3.p-1
     [:a.p-2.rounded-xl {:class "hover:bg-stone-400 hover:text-stone-300"
                         :href  "#"}
      "In Testing"]]
    [:div.m-3.p-1
     [:a.p-2.rounded-xl {:class "hover:bg-stone-400 hover:text-stone-300"
                         :href  "#"}
      "Work"]]]])
