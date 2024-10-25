(ns primeworker.core
  (:require [clojure.edn :as edn]
            [clj-http.client :as client]
            [fermat-primality.core :refer [probable-prime?]]))


(def primeseeker-server {:host "localhost"
                         :port 3000})

(defn request-number
  []
  (let [url (str "http://"
                 (:host primeseeker-server)
                 ":"
                 (:port primeseeker-server)
                 "/api/work")]
    (-> url
        (client/get {:accept :edn})
        :body
        edn/read-string)))

(defn send-result
  [n is-prime]
  (let [url  (str "http://"
                  (:host primeseeker-server)
                  ":"
                  (:port primeseeker-server)
                  "/api/work")
        body (assoc n :prime? is-prime)]
    (client/post url
                 {:body         (pr-str body)
                  :content-type :edn
                  :accept       :edn})))

(defn -main
  [& args]
  (println "Starting PrimeSeek worker...")
  (loop [n (request-number)]
    (println "Beginning Fermat Primality Test for" (:number n))
    (send-result n (probable-prime? (:number n) 1))
    (println "Done...")
    (println "Requesting next number.")
    (recur (request-number))))
