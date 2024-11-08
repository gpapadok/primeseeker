(ns primeseeker.primes-test
  (:require [clojure.test :refer :all]
            [next.jdbc :as jdbc]
            [primeseeker.primes :as sut]))

(def test-db {:dbtype "sqlite" :dbname "primes-test"})

(def ds (delay (jdbc/get-datasource test-db)))

(use-fixtures :each
  (fn [f]
    (let [seed-query   "insert into prime (number, is_prime) values (2, true), (3, true), (5, null)"
          delete-query "delete from prime"]
      (#'sut/ds-execute! ds seed-query)
      (binding [sut/*cache* (#'sut/create-atom-cache)]
        (f))
      (#'sut/ds-execute! ds delete-query))))

(deftest test-allocate-number-to-worker
  (testing "Test cache length"
    (let [l-1 (count (#'sut/cache-all-processing))
          _   (#'sut/allocate-number-to-worker! ds)
          l-2 (count (#'sut/cache-all-processing))]
      (is (= (inc l-1) l-2) "Cache length did not increase"))))
