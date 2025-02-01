(ns user
  (:require [primeseeker.core :refer [config]]
            [integrant.repl :as repl]
            [integrant.core :as ig]))

(repl/set-prep! (constantly config))

(def prep repl/prep)
(def init repl/init)
(def go repl/go)
(def halt repl/halt)
(def reset repl/reset)
