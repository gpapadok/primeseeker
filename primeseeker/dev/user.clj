(ns user
  (:require [primeseeker.core]
            [primeseeker.config :refer [config]]
            [integrant.repl :as repl]
            [integrant.repl.state :as state]
            [integrant.core :as ig]))

(repl/set-prep! (config))

(def prep repl/prep)
(def init repl/init)
(def go repl/go)
(def halt repl/halt)
(def reset repl/reset)
(def system state/system)
(def cfg state/config)
