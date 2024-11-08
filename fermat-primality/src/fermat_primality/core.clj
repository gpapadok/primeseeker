(ns fermat-primality.core)

;; Random Big Integer Generator
(defn- bit-length "Returns the number of bits for an integer" [n]
  (.bitLength (new java.math.BigInteger (str n))))

(defn- rand-n-bits
  "Generates a BigInteger with specified max number of bits"
  [nbits]
  (bigint (new java.math.BigInteger nbits (new java.util.Random))))

(defn- rand-bigint
  "Generates a uniformly distributed between low and high inclusive"
  ([high] (rand-bigint 0 high))
  ([low high]
   (if (>= low high)
     low
     (let [out (+ (rand-n-bits (bit-length (- high low))) low)]
       (if (<= out high)
         out
         (recur low high))))))
;;;

(defn- expmod "a ^ n mod k" [a n k]
  {:pre  [(>= a 0) (>= n 0) (>= k 0)]
   :post [(< % k)]}
  (cond (or (= a 1) (zero? n)) 1
        (even? n)              (expmod (mod (* a a) k) (/ n 2) k)
        :else                  (mod (* a (expmod a (dec n) k)) k)))

(defn probable-prime?
  "Is this a prime (probably)? - Perform a Fermat primality test"
  ([n] (if (even? n)
         (= n 2)
         (probable-prime? n 30)))
  ([n k]
   {:pre [(> n 0)]}
   (let [a   (rand-bigint (bigint 2) (bigint (- n 2)))
         one (expmod a (dec n) n)]
     (cond (not (= one 1)) false
           (zero? k)       true
           :else           (recur n (dec k))))))
