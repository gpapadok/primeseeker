(ns primeseeker.core
  (:require [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [taoensso.telemere :as t]
            [primeseeker.routes :refer [handler]]
            [primeseeker.config :refer [config]]
            [primeseeker.primes :refer [*cache*]]
            [primeseeker.util :refer [do-return now]]))

(defmethod ig/init-key :server/jetty [_ {:keys [port] :as opts}]
  (do-return (jetty/run-jetty
              (wrap-reload #'handler) ; Move to dev
              {:port  port
               :join? false})
    (t/log! :info ["Server started at port" port])))

(defmethod ig/halt-key! :server/jetty [_ server]
  (t/log! :info "Server stopped")
  (.stop server))

(defmethod ig/init-key :cache/invalidator [_ {:keys [expires-after run-every] :as opts}]
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

(defmethod ig/halt-key! :cache/invalidator [_ f]
  (future-cancel f))

(defn -main
  [& args]
  (ig/init config))
