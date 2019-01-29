(ns publicator-ext.domain.aggregates.user
  (:require
   [publicator-ext.domain.abstractions.id-generator :as id-generator]
   [publicator-ext.domain.abstractions.instant :as instant]
   [publicator-ext.domain.abstractions.aggregate :as aggregate]
   [publicator-ext.domain.util.validation :as validation]))

(def ^:const +states+ #{:active :deleted})

(defmethod aggregate/validator :user [chain]
  (-> chain
      (validation/attributes '[[(entity ?e)
                                [?e :db/ident :root]]]
                             [[:req :user/login string?]
                              [:req :user/login not-empty]
                              [:req :user/password-digest string?]
                              [:req :user/password-digest not-empty]
                              [:req :user/state +states+]])))

(defn build [tx-data]
  (let [id (id-generator/generate :user)]
    (aggregate/build :user id tx-data)))
