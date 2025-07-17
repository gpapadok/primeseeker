(ns build
  (:require [clojure.tools.build.api :as build]))

(def config
  "Configuration for building primeseeker"
  {:target-directory "target"
   :class-directory  "target/class"
   :project-basis    (build/create-basis {:project "deps.edn"})
   :main-namespace   'primeseeker.core
   :uberjar          "target/primeseeker-standalone.jar"})

(defn clean []
  "Clean build target directory"
  (let [directory (:target-directory config)]
    (build/delete {:path directory})
    (println (format "Target directory \"%s\" removed." directory))))

(defn uberjar [_]
  "Build an uberjar for primeseeker"
  (clean)
  (let [{:keys [class-directory main-namespace project-basis direct-linking uberjar]}
        config]
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
