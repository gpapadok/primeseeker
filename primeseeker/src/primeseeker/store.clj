(ns primeseeker.store
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(defprotocol PrimesStore
  "Datastore for natural numbers tested for primallty."
  (index-ds [ds n] "Get a specific natural number.")
  (get-all-numbers [ds] "Get all numbers.")
  (add-number! [ds p] "Insert a number to be processed.")
  (all-numbers [ds] "Get all numbers as vector.")
  (get-untested-numbers [ds] "Get only numbers that have not been tested for primality.")
  (update-tested-number! [ds n is-prime] "Update number with result of primality test."))

(def db {:dbtype "sqlite" :dbname "primes.db"})

(defn- ds-execute! [ds & query]
  (jdbc/execute! ds query {:builder-fn rs/as-unqualified-kebab-maps}))

(defn create-sqlite-datasource []
  (jdbc/get-datasource db))

(extend-type javax.sql.DataSource
  PrimesStore
  (index-ds [ds n]
    (ds-execute! ds "select * from natnum where num = ?" n))
  (get-all-numbers [ds]
    (ds-execute! ds "select * from natnum"))
  (add-number! [ds p]
    (ds-execute! ds "insert into natnum (num, is_prime, created_at) values (?, null, datetime('now'))" p))
  (all-numbers [ds]
    (mapv :num (ds-execute! ds "select num from natnum")))
  (get-untested-numbers [ds]
    (ds-execute! ds "select num from natnum where is_prime is null"))
  (update-tested-number! [ds n is-prime]
    (ds-execute! ds "update natnum set is_prime = ?, processed_at = datetime('now') where num = ?" is-prime n)))
