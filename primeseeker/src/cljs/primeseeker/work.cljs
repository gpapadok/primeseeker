(ns primeseeker.work
  (:require [primeseeker.ajax :as ajax]
            [primeseeker.fermat-primality :refer [probable-prime?]]))

(def pause-work (atom false))

;; TODO: response-format to edn
(defn work []
  (js/console.log "Starting to test numbers")
  (swap! pause-work (constantly false))
  (ajax/get< "/work"
             {:handler (fn [{:keys [number] :as response}]
                         (js/console.log "Start processing" number)
                         (let [is-prime? (probable-prime? (js/BigInt number))]
                           (js/console.log number "found to be prime:" is-prime?)
                           (ajax/post "/work"
                                      {:params (assoc response :prime? is-prime?)
                                       :handler (fn [response]
                                                  (if @pause-work
                                                    nil
                                                    (work)))
                                       :format :json})))
              :params {:asstring true}}))

(defn pause []
  (js/console.log "Pausing")
  (swap! pause-work (constantly true)))
