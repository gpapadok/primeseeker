(ns fermat-primality.core-test
  (:require [clojure.test :refer :all]
            [fermat-primality.core :as sut]))

(defn- power2 [n]
  (#'sut/power-bigint 2N n))

(deftest bit-length-test
  (testing "Bit length test"
    (doseq [i (range 1 80)]
      (is (= (#'sut/bit-length (inc (power2 i))) (inc i)))))
  (testing "Negative input test"
    (doseq [i (range 1 80)]
      (is (= (#'sut/bit-length (- 0 (inc (power2 i)))) (inc i))))))

(deftest rand-n-bits-test
  (testing "Test output upper bound"
    (doseq [i (range 80)]
      (is (< (#'sut/rand-n-bits i) (power2 i)))))
  (testing "Test output type"
   (is (= clojure.lang.BigInt (class (#'sut/rand-n-bits 10)))))
  (testing "Exception"
    (is (thrown? IllegalArgumentException (#'sut/rand-n-bits -10)))))

(deftest rand-bigint-test
  (testing "Test output bounds"
    (is (<= 0 (#'sut/rand-bigint 0 10) 10))
    (is (<= 100 (#'sut/rand-bigint 200 300) 300))
    (is (<= 123456789N (#'sut/rand-bigint 123456789N 567891234N) 567891234N)))
  (testing "Test output type"
    (is (= (class (#'sut/rand-bigint 100N 1000N)) clojure.lang.BigInt)))
  (testing "Test bad input"
    (is (= (#'sut/rand-bigint 5 2) 5))
    (is (= (#'sut/rand-bigint 0 -10) 0))))

(deftest power-bigint-test
  (testing "Test output type"
    (is (= (class (#'sut/power-bigint 10N 2N)) clojure.lang.BigInt)))
  (testing "Test output value"
    (is (= (#'sut/power-bigint 10N 2N) 100N))
    (is (= (#'sut/power-bigint -10N 3N) -1000N))
    (is (= (#'sut/power-bigint 10N 0N) 1N))
    (is (= (#'sut/power-bigint 10N 1N) 10N))
    (is (= (#'sut/power-bigint 10N -1N) 1N))))

(deftest probable-prime-test
  (testing "Test primality check"
    (is (sut/probable-prime? 2))
    (is (sut/probable-prime? 3))
    (is (not (sut/probable-prime? 4)))
    (is (sut/probable-prime? 5))
    (is (not (sut/probable-prime? 6)))
    (is (sut/probable-prime? 17))
    (is (not (sut/probable-prime? 7917)))
    (is (sut/probable-prime? 7919N)))
  (testing "Test negative input"
    (is (not (sut/probable-prime? -1)))
    (is (not (sut/probable-prime? -7919)))))
