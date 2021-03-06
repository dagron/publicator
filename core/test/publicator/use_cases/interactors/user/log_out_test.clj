(ns publicator.use-cases.interactors.user.log-out-test
  (:require
   [publicator.use-cases.interactors.user.log-out :as sut]
   [publicator.domain.aggregates.user :as user]
   [publicator.use-cases.services.user-session :as user-session]
   [publicator.use-cases.test.fakes :as fakes]
   [publicator.utils.test.instrument :as instrument]
   [publicator.use-cases.test.factories :as factories]
   [clojure.test :as t]))

(t/use-fixtures :each fakes/fixture)
(t/use-fixtures :once instrument/fixture)

(t/deftest main
  (let [user  (factories/create-user)
        _     (user-session/log-in! user)
        [tag] (sut/process)]
    (t/testing "success"
      (t/is (= ::sut/processed tag)))
    (t/testing "logged out"
      (t/is (user-session/logged-out?)))))

(t/deftest already-logged-out
  (let [[tag] (sut/process)]
    (t/testing "has error"
      (t/is (= ::sut/already-logged-out tag)))))
