(ns build
  (:require [clojure.tools.build.api :as build]))

(def build-config
  {:target-directory "target"
   :class-directory  "target/class"
   :project-basis    (build/create-basis {:project "deps.edn"})
   :main-namespace   'primeseeker.core
   :uberjar          "target/primeseeker.jar"})

(defn clean [_]
  (build/delete {:path (:target-directory build-config)})
  (println (format "Target directory \"%s\" removed." (:target-directory build-config))))

(defn uberjar [_]
  (clean nil)
  (let [{:keys [class-directory main-namespace project-basis direct-linking uberjar]}
        build-config]
    (build/copy-dir {:src-dirs   ["src/clj" "src/cljs" "resources"]
                     :target-dir class-directory})
    (build/compile-clj {:class-dir    class-directory
                        :src-dirs     ["src/clj" "src/cljs"]
                        :ns-compile   `[taoensso.telemere.impl
                                        taoensso.encore.signals
                                        ~main-namespace]
                        :basis        project-basis
                        :compile-opts {:direct-linking false}})
    (build/uber {:basis     project-basis
                 :class-dir class-directory
                 :main      main-namespace
                 :uber-file uberjar})))
