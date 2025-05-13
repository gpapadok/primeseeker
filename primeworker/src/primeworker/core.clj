(ns primeworker.core
  (:require [clojure.edn :as edn]
            [babashka.http-client :as client]
            [fermat-primality.core :refer [probable-prime?]]))

(defn- request-work [server]
  (-> (str server "/api/work")
      (client/get {:headers {:accept "application/edn"}})
      :body
      edn/read-string))

(defn- send-result [server n is-prime]
  (let [body (assoc n :prime? is-prime)]
    (client/post (str server "/api/work")
                 {:body         (pr-str body)
                  :headers {:content-type "application/edn"
                            :accept       "application/edn"}})))

(defn- work [server]
  (let [get-number    (partial request-work server)
        update-number (partial send-result server)]
    (println "Connecting to" server "...")
    (loop [n (get-number)]
      (println "Testing" (:number n))
      (update-number n (probable-prime? (:number n)))
      (recur (get-number)))))

(defn -main
  [& [host port :as args]]
  (let [server        (str "http://"
                           (or host "localhost")
                           ":"
                           (or port 3000))]
    (work server)))
