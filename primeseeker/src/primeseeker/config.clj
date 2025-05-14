(ns primeseeker.config
  (:require [integrant.core :as ig]))

(defn config []
  (ig/read-string (slurp "resources/config.edn")))
