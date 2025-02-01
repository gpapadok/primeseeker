(ns primeseeker.core
  (:require [primeseeker.routes :refer [handler]]
            [integrant.core :as ig]
            [muuntaja.middleware :as muuntaja]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [taoensso.telemere :as t]
            [primeseeker.primes :refer [*cache*]]
            [primeseeker.cache :refer [now]]))

(def config
  (ig/read-string (slurp "resources/config.edn")))

;;; Util ;;;
(defmacro do-return [form & body]
  `(let [res# ~form]
     ~@body
     res#))

(defn dump [x]
  (prn x)
  x)
;;;;;;;;;;;;

(defmethod ig/init-key :server/jetty [_ {:keys [port] :as opts}]
  (do-return (jetty/run-jetty
              (-> handler
                  muuntaja/wrap-format
                  wrap-reload)
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
