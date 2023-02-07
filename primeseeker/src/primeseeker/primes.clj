(ns primeseeker.primes)

(def primes-db (atom {2 {:prime? true}
                      3 {:prime? true}
                      5 nil}))

(defn index-db
  [n]
  (@primes-db n))

(defn- add-number!
  [p]
  (swap! primes-db
         (fn [primes-col new-p]
           (conj primes-col {new-p nil})) p))

(defn create-and-add-number!
  []
  (let [numbers     (keys @primes-db)
        max-n       (apply max numbers)
        next-number (+ max-n 2)]
    (add-number! next-number)
    next-number))

(defn get-first-available
  []
  (if-let [first-available (->> @primes-db
                                vec
                                (sort-by first)
                                (drop-while #(-> %
                                                 second
                                                 nil?
                                                 not))
                                ffirst)]
    first-available
    (create-and-add-number!)))

(defn allocate-number-to-worker!
  []
  (let [next-number (get-first-available)]
    (swap! primes-db
           (fn [primes-col n]
             (assoc primes-col n {:processing true}))
           next-number)
    next-number))

(defn update-number!
  [n is-prime]
  (swap! primes-db
         (fn [primes-col n is-prime]
           (assoc primes-col n {:prime? is-prime}))
         n
         is-prime))
