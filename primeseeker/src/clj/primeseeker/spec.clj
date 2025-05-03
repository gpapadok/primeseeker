(ns primeseeker.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::limit (s/nilable int?))
(s/def ::offset (s/nilable int?))
(s/def ::pagination (s/keys :opt [::limit ::offset]))

(s/def ::asstring boolean?)
