(ns primeseeker.util)

(defn now [] (java.time.Instant/now))

(defmacro do-return [form & body]
  `(let [res# ~form]
     ~@body
     res#))

(defn dump [x]
  (prn x)
  x)
