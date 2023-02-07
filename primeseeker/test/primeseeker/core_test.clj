(ns primeseeker.core-test
  (:require [clojure.test :refer :all]
            [primeseeker.core :refer :all]))

(deftest test-prime-index
  (testing "test success"
    (is (= 0 (prime-index 2)))
    (is (= 1 (prime-index 3)))
    (is (= 2 (prime-index 5)))
    (is (= 3 (prime-index 7))))
  (testing "test failure"
    (is (thrown? AssertionError (prime-index 4)))
    (is (thrown? AssertionError (prime-index -1)))))

(deftest test-create-and-add
  (testing "test db length"
    (let [l-1 (count @primes-db)
          _   (create-and-add-number!)
          l-2 (count @primes-db)]
      (is (= (inc l-1) l-2)))))

(deftest test-get-first-available
  (testing "test nil value"
    (is (-> (get-first-available) vals first nil?))))

