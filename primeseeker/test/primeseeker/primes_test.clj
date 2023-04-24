(ns primeseeker.primes-test
  (:require [clojure.test :refer :all]
            [primeseeker.primes :refer :all]))

(deftest test-create-and-add
  (testing "test db length"
    (let [l-1 (count @primes-db)
          _   (create-and-add-number!)
          l-2 (count @primes-db)]
      (is (= (inc l-1) l-2)))))
