(ns primeseeker.config
  (:require [integrant.core :as ig]))

(def config
  (ig/read-string (slurp "resources/config.edn")))
