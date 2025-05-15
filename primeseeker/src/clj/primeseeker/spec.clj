(ns primeseeker.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::limit (s/nilable nat-int?))
(s/def ::offset (s/nilable nat-int?))
(s/def ::pagination (s/keys :opt [::limit ::offset]))

(s/def ::asstring boolean?)

(s/def ::number (s/or :i nat-int? :s string?))
(s/def ::proc-id uuid?)
(s/def ::prime? boolean?)
