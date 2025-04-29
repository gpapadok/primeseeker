(ns primeseeker.work
  (:require [primeseeker.ajax :as ajax]
            [primeseeker.fermat-primality :refer [probable-prime?]]))

(def pause-work (atom false))

(defn work []
  (js/console.log "Starting to test numbers")
  (swap! pause-work (constantly false))
  (ajax/get< "/work"
             {:handler (fn [{:keys [number] :as response}]
                         (js/console.log "Start processing" number)
                         (ajax/post "/work"
                                    {:params (assoc response :prime? (probable-prime? number))
                                     :handler (fn [response]
                                                (js/console.log "Finished processing" number)
                                                (if @pause-work
                                                  nil
                                                  (work)))
                                     :format :edn
                                     :response-format :edn}))
              :params {:asstring true}
              :response-format :edn}))

(defn pause []
  (js/console.log "Pausing")
  (swap! pause-work (constantly true)))
