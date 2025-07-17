(ns fermat-primality.core-test
  (:require [clojure.test :refer :all]
            [fermat-primality.core :as sut]))

(defn- power2 [n]
  (reduce * (repeat n 2N)))

(defmacro is-repeatedly
  "Tests wether the output of the function satisfies a predicate multiple times.
  Good for testing stochastic functions."
  [pred function]
  `(is (every? ~pred (repeatedly 100 ~function))))

(deftest bit-length-test
  (testing "Bit length test"
    (doseq [i (range 1 80)]
      (is (= (inc i) (#'sut/bit-length (inc (power2 i)))))))
  (testing "Negative input test"
    (doseq [i (range 1 80)]
      (is (= (inc i) (#'sut/bit-length (- 0 (inc (power2 i)))))))))

(deftest rand-n-bits-test
  (testing "Test output upper bound"
    (doseq [i (range 80)]
      (is (> (power2 i) (#'sut/rand-n-bits i)))))
  (testing "Test output type"
    (is (= clojure.lang.BigInt (class (#'sut/rand-n-bits 10)))))
  (testing "Exception"
    (is (thrown? IllegalArgumentException (#'sut/rand-n-bits -10)))))

(deftest rand-bigint-test
  (testing "Test output bounds"
    (is-repeatedly #(<= 0 % 10) #(#'sut/rand-bigint 0 10))
    (is-repeatedly #(<= 0 % 10) #(#'sut/rand-bigint 0 10))
    (is-repeatedly #(<= 20 % 300) #(#'sut/rand-bigint 20 300))
    (is-repeatedly #(<= 123456789N % 567891234N) #(#'sut/rand-bigint 123456789N 567891234N)))
  (testing "Test output type"
    (is (= clojure.lang.BigInt (class (#'sut/rand-bigint 100N 1000N)))))
  (testing "Test bad input"
    (is-repeatedly #(= 5 %) #(#'sut/rand-bigint 5 2))
    (is-repeatedly #(= 0 %) #(#'sut/rand-bigint 0 -10))))

(deftest expmod-test
  (testing "Test output type"
    (is (=  clojure.lang.BigInt (class (#'sut/expmod 10N 2N 1024N)))))
  (testing "Test output value"
    (is (= 100N (#'sut/expmod 10N 2N 101N)))
    (is (= 1N (#'sut/expmod 10N 0N 80N)))
    (is (= 10N (#'sut/expmod 10N 1N 123N)))
    (is (= 1024 (#'sut/expmod 2 10 2000))))
  (testing "Invalid input"
    (is (thrown? AssertionError (#'sut/expmod -10N 3N 10001N)))
    (is (thrown? AssertionError (#'sut/expmod 10N -1N 2N)))
    (is (thrown? AssertionError (#'sut/expmod 10N 1N -2N)))))

(deftest probable-prime-test
  (testing "Test primality check"
    (is (sut/probable-prime? 2))
    (is (sut/probable-prime? 3))
    (is (not (sut/probable-prime? 4)))
    (is (sut/probable-prime? 5))
    (is (not (sut/probable-prime? 6)))
    (is (sut/probable-prime? 17))
    (is (not (sut/probable-prime? 7917)))
    (is (sut/probable-prime? 7919N))
    (is (sut/probable-prime? 32212254719N))
    (is (not (sut/probable-prime? 4776913109852041418248056622882488317)))
    (is (sut/probable-prime? 4776913109852041418248056622882488319))
    (is (sut/probable-prime? 76745640142636677578016477075117615076923039213553)))
  (testing "Test negative input"
    (is (thrown? AssertionError (sut/probable-prime? -1)))
    (is (thrown? AssertionError (sut/probable-prime? -7919)))))
