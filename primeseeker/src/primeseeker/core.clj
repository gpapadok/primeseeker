(ns primeseeker.core
  (:require [primeseeker.routes :refer [handler]]
            [integrant.core :as ig]
            [muuntaja.middleware :as muuntaja]
            [reitit.ring.middleware.muuntaja :as rmuuntaja]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]))

(def config
  (ig/read-string (slurp "resources/config.edn")))

(defmethod ig/init-key :server/jetty [_ {:keys [port] :as opts}]
  (jetty/run-jetty
   (-> handler
       muuntaja/wrap-format
       wrap-reload)
   {:port  port
    :join? false}))

(defmethod ig/halt-key! :server/jetty [_ server]
  (.stop server))

(defn go [& args]
  (def system
    (ig/init config)))

(defn halt! [& args]
  (ig/halt! system))

(defn reset [& args]
  (halt!) (go))

(defn -main
  [& args]
  (go))
