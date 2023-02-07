(ns fermat-primality.core-test
  (:require [clojure.test :refer :all]
            [fermat-primality.core :refer :all]))


(defn power2
  [n]
  (#'fermat-primality.core/power-bigint 2N n))


(deftest bit-length-test
  (testing "Bit length test"
    (loop [i 1]
      (if (< i 80)
        (do (is (= (#'fermat-primality.core/bit-length (inc (power2 i))) (inc i)))
            (recur (inc i))))))
  (testing "Negative input test"
    (loop [i 1]
      (if (< i 80)
        (do (is (= (#'fermat-primality.core/bit-length (- 0 (inc (power2 i)))) (inc i)))
            (recur (inc i)))))))


(deftest rand-n-bits-test
  (testing "Test output upper bound"
    (loop [i 0]
      (if (< i 80)
        (do (is (< (#'fermat-primality.core/rand-n-bits i) (power2 i)))
            (recur (inc i))))))
  (testing "Test output type"
   (is (= clojure.lang.BigInt (class (#'fermat-primality.core/rand-n-bits 10)))))
  (testing "Exception"
    (is (thrown? IllegalArgumentException (#'fermat-primality.core/rand-n-bits -10)))))


(deftest rand-bigint-test
  (testing "Test output bounds"
    (is (<= 0 (#'fermat-primality.core/rand-bigint 0 10) 10))
    (is (<= 100 (#'fermat-primality.core/rand-bigint 200 300) 300))
    (is (<= 123456789N (#'fermat-primality.core/rand-bigint 123456789N 567891234N) 567891234N)))
  (testing "Test output type"
    (is (= (class (#'fermat-primality.core/rand-bigint 100N 1000N)) clojure.lang.BigInt)))
  (testing "Test bad input"
    (is (= (#'fermat-primality.core/rand-bigint 5 2) 5))
    (is (= (#'fermat-primality.core/rand-bigint 0 -10) 0))))


(deftest power-bigint-test
  (testing "Test output type"
    (is (= (class (#'fermat-primality.core/power-bigint 10N 2N)) clojure.lang.BigInt)))
  (testing "Test output value"
    (is (= (#'fermat-primality.core/power-bigint 10N 2N) 100N))
    (is (= (#'fermat-primality.core/power-bigint -10N 3N) -1000N))
    (is (= (#'fermat-primality.core/power-bigint 10N 0N) 1N))
    (is (= (#'fermat-primality.core/power-bigint 10N 1N) 10N))
    (is (= (#'fermat-primality.core/power-bigint 10N -1N) 1N))))


(deftest probable-prime-test
  (testing "Test primality check"
    (is (probable-prime? 2))
    (is (probable-prime? 3))
    (is (not (probable-prime? 4)))
    (is (probable-prime? 5))
    (is (not (probable-prime? 6)))
    (is (probable-prime? 17))
    (is (not (probable-prime? 7917)))
    (is (probable-prime? 7919N)))
  (testing "Test negative input"
    (is (not (probable-prime? -1)))
    (is (not (probable-prime? -7919)))))
