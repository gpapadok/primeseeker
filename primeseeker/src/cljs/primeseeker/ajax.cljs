(ns primeseeker.ajax
  (:require [ajax.core :as ajax]))

;; TODO: Maybe get this from document
(def host "http://localhost:3000/api")

(defn error-handler [error-response]
  (js/console.log error-response))

(defn get< [uri & opts]
  (ajax/GET (str host uri)
            (merge {:error-handler error-handler}
                   opts)))

(defn post [uri & opts]
  (ajax/POST (str host uri)
             (merge {:error-handler error-handler}
                    opts)))
