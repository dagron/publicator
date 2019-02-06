(ns publicator-ext.domain.aggregates.admin-test
  (:require
   [publicator-ext.domain.aggregates.admin :as sut]
   [publicator-ext.domain.test.fakes :as fakes]
   [clojure.test :as t]))

(t/use-fixtures :each fakes/fixture)

(t/deftest build
  (let [user-id 1
        tx-data [{:db/ident    :root
                  :admin/state :active}]
        admin   (sut/build user-id tx-data)]
    (t/is (some? admin))))
