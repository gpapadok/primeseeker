(ns user
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))

(def db {:dbtype "sqlite" :dbname "primes"})
(def test-db {:dbtype "sqlite" :dbname "primes-test"})

(def migrations-dir "resources/migrations")

(def config
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
