(ns primeworker
  (:require [clojure.edn :as edn]
            [babashka.http-client :as http]
            [babashka.cli :as cli]
            [fermat-primality.core :refer [probable-prime?]]))

(def cli-options {:port {:default 3000 :coerce :long}
                  :host {:default "localhost" :coerce :string}})

(def *work-endpoint* "/api/work")

(defn wrap-edn [request]
  (fn [& args]
    (let [response (apply request args)]
      (if (not= (:status response) 200)
        {:message (str "Error fetching from " (:uri response))}
        (edn/read-string (:body response))))))

(def get (wrap-edn #(http/get % {:headers {:accept "application/edn"}})))
(def post (wrap-edn #(http/post %1 {:body    (pr-str %2)
                                    :headers {:content-type "application/edn"
                                              :accept       "application/edn"}})))

(defn- request-work [url]
  (get (str url *work-endpoint*)))

(defn- send-result [url n is-prime]
  (post (str url *work-endpoint*) (assoc n :prime? is-prime)))

(defn- work [url]
  (let [get-number    (partial request-work url)
        update-number (partial send-result url)]
    (println "Connecting to" url "...")
    (loop [n (get-number)]
      (println "Testing" (:number n))
      (update-number n (probable-prime? (:number n)))
      (recur (get-number)))))

(defn -main
  [& args]
  (let [opts (cli/parse-opts *command-line-args* {:spec cli-options})]
    (work (format "http://%s:%s" (:host opts) (:port opts)))))
