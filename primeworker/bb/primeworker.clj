(ns primeworker
  (:require [clojure.edn :as edn]
            [babashka.http-client :as http]
            [babashka.cli :as cli]
            [fermat-primality.core :refer [probable-prime?]]))

(def cli-options {:port {:default 3000 :coerce :long}
                  :host {:default "localhost" :coerce :string}})

(defn- request-work [server]
  (-> (str server "/api/work")
      (http/get {:headers {:accept "application/edn"}})
      :body
      edn/read-string))

(defn- send-result [server n is-prime]
  (let [body (assoc n :prime? is-prime)]
    (http/post (str server "/api/work")
               {:body    (pr-str body)
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
  [& args]
  (let [opts (cli/parse-opts *command-line-args* {:spec cli-options})]
    (work (format "http://%s:%s" (:host opts) (:port opts)))))
