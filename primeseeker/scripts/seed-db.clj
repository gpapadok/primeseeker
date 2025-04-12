(ns user
  (:require [ragtime.jdbc :as rjdbc]
            [ragtime.repl]))

(def db {:dbtype "sqlite" :dbname "primes.db"})
(def test-db {:dbtype "sqlite" :dbname "primes-test.db"})

(def migrations-dir "resources/migrations")

(def migrations-config
  {:datastore (rjdbc/sql-database db)
   ;; NOTE: Only works with relative for some reason
   :migrations (rjdbc/load-resources (str "../" migrations-dir))})

;; TODO: Create separate module for time stuff
(defn- current-ts []
  (.format (java.time.LocalDateTime/now)
           (java.time.format.DateTimeFormatter/ofPattern "yyyyMMddHHmmssSSSSSS")))

(defn new-migration [name]
  (let [migration-file     (partial clojure.java.io/file migrations-dir)
        migration-filename (partial str (current-ts) "-")]
    (spit (migration-file (migration-filename name ".up.sql")) "")
    (spit (migration-file (migration-filename name ".down.sql")) "")))

(defn rollback []
  (ragtime.repl/rollback migrations-config))

(defn migrate []
  (ragtime.repl/migrate migrations-config))
