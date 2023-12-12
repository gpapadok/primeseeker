(ns user
  (:require [next.jdbc :as jdbc]))

(def db {:dbtype "sqlite" :dbname "primes"})

(def ds (jdbc/get-datasource db))

(jdbc/execute! ds ["
create table if not exists prime (
    number int auto_increment primary key,
    is_prime boolean
)"])

(jdbc/execute! ds ["
insert into prime (number, is_prime)
values (2, true), (3, true), (5, null)
"])
