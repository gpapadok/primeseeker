(ns primeseeker.ajax
  (:require [ajax.core :as ajax]))

;; TODO: Maybe get this from document
(def host "http://localhost:3000/api")

(defn- error-handler [error-response]
  (js/console.log error-response))

(def default-options
  {:error-handler   error-handler
   :response-format :json
   :keywords?       true})

(defn get< [uri opts]
  (ajax/GET (str host uri)
            (merge default-options opts)))

(defn post [uri opts]
  (ajax/POST (str host uri)
             (merge default-options opts)))
