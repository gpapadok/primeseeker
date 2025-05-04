(ns fermat-primality.core)

(def !0 #?(:clj 0N :cljs (js/BigInt 0)))
(def !1 #?(:clj 1N :cljs (js/BigInt 1)))
(def !2 #?(:clj 2N :cljs ((js/BigInt 2))))

#?(:clj
   (do
     ;; Random Big Integer Generator
     (defn- bit-length "Returns the number of bits for an integer" [n]
       (.bitLength (new java.math.BigInteger (str n))))

     (defn- rand-n-bits
       "Generates a BigInteger with specified max number of bits"
       [nbits]
       (bigint (new java.math.BigInteger nbits (new java.util.Random)))))

   :cljs
   (do
     (defn- bigint? [n]
       (= (type n) js/BigInt))

     (defn- even? [n]
       (if (bigint? n)
         (= (mod n !2) !0)
         (cljs.core/even? n)))

     (defn- zero? [n]
       (if (bigint? n)
         (= n !0)
         (cljs.core/zero? n)))

     (defn- dec [n]
       (if (bigint? n)
         (- n !1)
         (cljs.core/dec n)))))

;; TODO: This could be cleaned up
#?(:clj
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

   :cljs
   (defn rand-bigint
     ([high]
      (->> (str high)
           (reduce (fn [{:keys [<high digits] :as acc} digit]
                     (if <high
                       (update acc
                               :digits (fn [n x] (cons x n))
                               (str (rand-int 10)))
                       (let [idigit    (parse-long digit)
                             new-digit (rand-int (inc idigit))]
                         (-> acc
                             (update :digits (fn [n x] (cons x n))
                                     (str new-digit))
                             (assoc :<high (< new-digit idigit))))))
                   {:<high  false
                    :digits '()})
           :digits
           reverse
           (apply str)
           js/BigInt))
     ([low high]
      (assert (< low high))
      (+ (rand-bigint (- high low)) low))))

;;;

(defn- expmod "a ^ n mod k" [a n k]
  {:pre  [(>= a 0) (>= n 0) (>= k 0)]
   :post [(< % k)]}
  (cond (or (= a !1) (zero? n)) !1
        (even? n)              (expmod (mod (* a a) k) (/ n 2) k)
        :else                  (mod (* a (expmod a (dec n) k)) k)))

(defn probable-prime?
  "Is this a prime (probably)? - Perform a Fermat primality test"
  ([n] (if (even? n)
         (= n !2)
         (probable-prime? n 30)))
  ([n k]
   {:pre [(> n 0)]}
   (let [a   (rand-bigint !2 (- n !2))
         one (expmod a (dec n) n)]
     (cond (not (= one !1)) false
           (zero? k)       true
           :else           (recur n (dec k))))))
