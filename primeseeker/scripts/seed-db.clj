(ns user
  (:require [next.jdbc :as jdbc]))

(def db {:dbtype "sqlite" :dbname "primes"})
(def test-db {:dbtype "sqlite" :dbname "primes-test"})

(def ds (jdbc/get-datasource db))
(def test-ds (jdbc/get-datasource test-db))

(def create-table "
create table if not exists prime (
    number int auto_increment primary key,
    is_prime boolean
)
")

(def seed "
insert into prime (number, is_prime)
values (2, true), (3, true), (5, null)
")

(jdbc/execute! ds [create-table])
(jdbc/execute! ds [seed])
(jdbc/execute! test-ds [create-table])
(jdbc/execute! test-ds [seed])
