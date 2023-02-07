(ns fermat-primality.core)


;; Random Big Integer Generator
(defn- bit-length
  "Returns the number of bits for an integer"
  [n]
  (.bitLength (new java.math.BigInteger (str n))))


(defn- rand-n-bits
  "Generates a BigInteger with specified max number of bits"
  [nbits]
  (bigint (new java.math.BigInteger nbits (new java.util.Random))))


(defn- rand-bigint
  "Generates a uniformly distributed between low and high inclusive"
  [low high]
  (if (>= low high)
    low
    (loop []
      (let [inter (rand-n-bits (bit-length (- high low)))
            out (+ inter low)]
        (if (<= out high)
          out
          (recur))))))


;; Power for Big Integer exponents
(defn- power-bigint
  "Calculates a power for a BigInt exponent. Only works for positive exponents."
  [x a]
  (loop [i a
         p 1N]
    (if (> i 0)
      (recur (dec i) (* p x))
      p)))


;; Fermat primality check
(defn probable-prime?
  ([n] (probable-prime? n 30))
  ([n k]
   (if (= (mod n 2) 0)
     (= n 2)
     (loop [i 0]
       (let [a (rand-bigint (bigint 2) (bigint (- n 2)))
             one (mod (power-bigint a (dec n)) n)]
         (if (= one 1)
           (if (< i k)
             (recur (inc i))
             true)
           false))))))
