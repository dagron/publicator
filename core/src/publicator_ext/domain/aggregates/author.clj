(ns publicator-ext.domain.aggregates.author
  (:require
   [publicator-ext.domain.abstractions.aggregate :as aggregate]
   [publicator-ext.domain.abstractions.id-generator :as id-generator]
   [publicator-ext.domain.util.validation :as validation]
   [publicator-ext.domain.languages :as langs]
   [publicator-ext.util :as u]))

(def ^:const +states+ #{:active :archived})
(def ^:const +stream-participation-roles+ #{:regular :admin})

(defmethod aggregate/schema :author [_]
  {:author.translation/author          {:db/valueType :db.type/ref}
   :author.stream-participation/author {:db/valueType :db.type/ref}})

(defmethod aggregate/validator :author [chain]
  (-> chain
      (validation/types [:author/state +states+]
                        [:author.translation/lang langs/+languages+]
                        [:author.translation/first-name string?]
                        [:author.translation/last-name string?]
                        [:author.stream-participation/role +stream-participation-roles+]
                        [:author.stream-participation/stream-id pos-int?])

      (validation/required-for '{:find  [[?e ...]]
                                 :where [[?e :db/ident :root]]}
                               [:author/state some?])
      (validation/required-for '{:find  [[?e ...]]
                                 :where [[?e :author.translation/author :root]]}
                               [:author.translation/lang some?]
                               [:author.translation/first-name not-empty]
                               [:author.translation/last-name not-empty])
      (validation/required-for '{:find  [[?e ...]]
                                 :where [[?e :author.stream-participation/author :root]]}
                               [:author.stream-participation/role some?]
                               [:author.stream-participation/stream-id some?])

      (validation/query '{:find  [[?e ...]]
                          :where [[?e :db/ident :root]]}
                        '{:find  [[?lang ...]]
                          :in    [$ ?e]
                          :with  [?trans]
                          :where [[?trans :author.translation/author ?e]
                                  [?trans :author.translation/lang ?lang]]}
                        u/match? langs/+languages+)
      (validation/query '{:find  [[?e ...]]
                          :where [[?e :db/ident :root]]}
                        '{:find  [[?stream-id ...]]
                          :in    [$ ?e]
                          :with  [?part]
                          :where [[?part :author.stream-participation/author ?e]
                                  [?part :author.stream-participation/stream-id ?stream-id]]}
                        u/distinct?)))

(defn build [user-id tx-data]
  (aggregate/build :author user-id tx-data))
