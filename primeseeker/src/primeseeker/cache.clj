(ns primeseeker.cache)

(defprotocol PrimesCache
  "Cache to store numbers being processed to evaluate primality"
  (insert! [this number uuid] "Insert number")
  (delete! [this number] "Delete number")
  (inspect [this] "Get all"))

(defn now [] (java.time.Instant/now))

(defn create-atom-cache []
  (let [cache (atom {})]
    (reify PrimesCache
      (insert! [this number uuid]
        (swap! cache
               #(assoc % number {:processing true
                                 :proc-id    uuid
                                 :started-at (now)})))
      (delete! [this number] (swap! cache #(dissoc % number)))
      (inspect [this] @cache))))
