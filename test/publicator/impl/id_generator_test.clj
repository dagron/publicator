(ns publicator.impl.id-generator-test
  (:require
   [clojure.test :as t]
   [publicator.impl.id-generator :as sut]
   [publicator.impl.test-db :as test-db]
   [publicator.domain.abstractions.id-generator :as id-generator]))

(t/use-fixtures :each
  (fn [t]
    (with-bindings (sut/binding-map test-db/data-source)
      (t))))

(t/deftest generate
    (t/is (pos-int? (id-generator/generate))))