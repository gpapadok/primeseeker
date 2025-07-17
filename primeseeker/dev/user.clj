(ns user
  (:require [primeseeker.system]
            [primeseeker.config :refer [config]]
            [integrant.repl :as repl]
            [integrant.repl.state :as state]
            [integrant.core :as ig]))

(defn set-prep!
  ([] (set-prep! :dev))
  ([profile] (repl/set-prep! #(config profile))))

(def prep repl/prep)
(def init repl/init)
(defn go [] (set-prep!) (repl/go))
(def halt repl/halt)
(def reset repl/reset)
(defn system [] state/system)
(defn cfg [] state/config)
