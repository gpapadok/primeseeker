(ns primeseeker.ajax
  (:require [ajax.core :as ajax]))

(goog-define API_URL "")

(defn- error-handler [error-response]
  (js/console.log error-response))

(def default-options
  {:error-handler   error-handler
   :response-format :json
   :keywords?       true})

(defn get< [uri opts]
  (ajax/GET (str API_URL uri)
            (merge default-options opts)))

(defn post [uri opts]
  (ajax/POST (str API_URL uri)
             (merge default-options opts)))
