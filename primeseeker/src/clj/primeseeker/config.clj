(ns primeseeker.config
  (:require [aero.core :as aero]
            [integrant.core :as ig]))

(defmethod aero/reader 'ig/ref
  [_ _ value]
  (ig/ref value))

(defn config
  ([] (config :dev))
  ([profile]
   (aero/read-config (clojure.java.io/resource "config.edn") {:profile profile})))
