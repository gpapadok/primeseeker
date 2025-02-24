(ns primeseeker.ui.primes
  (:require [primeseeker.ui.main :refer [main]]
            [hiccup2.core :as h]
            [ring.util.codec]))

(defn- move-offset [op limit offset count]
  (let [res (op offset limit)]
    (cond (<= res 0)               0
          (>= res (- count limit)) (- count limit)
          :else                    res)))

(defn- pagination-query-string [limit offset]
  (ring.util.codec/form-encode {:limit limit
                                :offset offset}))

(defn- previous-page [limit offset count]
  (pagination-query-string limit
                           (move-offset - limit offset count)))

(defn- next-page [limit offset count]
  (pagination-query-string limit
                           (move-offset + limit offset count)))

(defn- primes-table [primes limit offset count]
  [:div.m-10.ml-25
   [:table.table-auto.width
    [:thead
     [:tr.border-b-2.border-zinc-300
      [:th.border-x-2.border-zinc-300.p-2 "Prime"]
      [:th.border-x-2.border-zinc-300.p-2 "Processed at"]]]
    (into [:tbody]
          (mapv (fn [p]
                  [:tr {:class "last:border-b-2 last:border-zinc-300"}
                   [:td.border-x-2.border-zinc-300.p-4
                    (str (:num p))]
                   [:td.border-x-2.border-zinc-300.p-4
                    (:processed-at p)]])
                primes))]
   [:div.flex
    [:button.p-3.text-2xl
     {:class "hover:underline hover:underline-offset-4 hover:text-orange-600"}
     [:a.text-lime-500 {:href (str "/primes?" (previous-page limit offset count))}
      "<<"]]
    [:div.flex-1]
    [:button.p-3.text-2xl
     {:class "hover:underline hover:underline-offset-4 hover:text-orange-600"}
     [:a.text-lime-500 {:href (str "/primes?" (next-page limit offset count))}
      ">>"]]]])

(defn primes [primes {:keys [limit offset count]
                      :or {limit 20 offset 0}
                      :as pagination}] ;; [{:num 10 :processed-at timestamp}]
  (main (primes-table primes
                      (Integer/parseInt limit) ; TODO: coercion
                      (Integer/parseInt offset)
                      count)))
