(ns primeseeker.view
  (:require [primeseeker.ajax :as ajax]
            [primeseeker.fermat-primality :refer [probable-prime?]]))

(def pause-work (atom false))

(defn work []
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
  (swap! pause-work (constantly true)))

(defn work-button []
  [:div#work
   [:button
    {:on-click work}
    "Worki!"]])

(defn mitsos []
  (js/alert "mitsos!"))
