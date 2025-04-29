(ns primeseeker.fermat-primality)

(def !0 (js/BigInt 0))
(def !1 (js/BigInt 1))
(def !2 (js/BigInt 2))
(def !30 (js/BigInt 30))

(defn dump [x] (prn x) x)

(defn !rand-int
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
   (+ (!rand-int (- high low)) low)))

;;;

(defn- !even? [n]
  (= (mod n !2) !0))

(defn- !dec [n]
  (- n !1))

(defn- !zero? [n]
  (= n !0))

(defn- expmod "a ^ n mod k" [a n k]
  (cond (or (= a !1) (= n !0)) !1
        (!even? n)             (expmod (mod (* a a) k) (/ n !2) k)
        :else                  (mod (* a (expmod a (- n !1) k)) k)))

(defn probable-prime?
  "Is this a prime (probably)? - Perform a Fermat primality test"
  ([n] (if (!even? n)
         (= n !2)
         (probable-prime? n !30)))
  ([n k]
   (let [a   (!rand-int !2 (- n !1))
         one (expmod a (!dec n) n)]
     (cond (not (= one !1)) false
           (!zero? k)       true
           :else           (recur n (!dec k))))))
