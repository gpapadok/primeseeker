(ns primeseeker.system
  (:require [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [taoensso.telemere :as t]
            [primeseeker.routes :refer [app]]
            [primeseeker.config :refer [config]]
            [primeseeker.primes :refer [*cache* create-datasource]]
            [primeseeker.util :refer [do-return now]]))

(defn initialize []
  (ig/init (config)))

(defmethod ig/init-key ::jetty [_ {:keys [port handler] :as opts}]
  (do-return (jetty/run-jetty
              ;; (wrap-reload #'handler) ; Move to dev
              handler
              {:port  port
               :join? false})
    (t/log! :info ["Server started at port" port])))

(defmethod ig/halt-key! ::jetty [_ server]
  (t/log! :info "Server stopped")
  (.stop server))

(defmethod ig/init-key ::cache-invalidator [_ {:keys [expires-after run-every] :as opts}]
  (future (loop []
            (Thread/sleep (* run-every 1000))
            (doseq [number (->> (. *cache* inspect)
                                (filter (fn [[_ v]]
                                          (-> (now)
                                              (.minusSeconds expires-after)
                                              (.isAfter (:started-at v)))))
                                (map first))]
              (. *cache* delete! number)
              (t/log! :info ["Cache - number invalidated" number]))
            (recur))))

(defmethod ig/halt-key! ::cache-invalidator [_ f]
  (future-cancel f))

(defmethod ig/init-key ::router [_ opts]
  (app opts))

(defmethod ig/init-key ::store [_ config]
  (create-datasource))
