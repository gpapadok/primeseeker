(ns primeseeker.ui.work
  (:require [primeseeker.ui.main :refer [main]]
            [hiccup2.core :as h]))

(defn work []
  (main [:div
         [:button.p-5.bg-green-600.border-100.border-black
          {:onclick "primeseeker.core.do_something()"}
          "Mitsos"]]))
