(ns publicator.controllers.post.create-test
  (:require
   [publicator.ring.handler :as handler]
   [publicator.interactors.post.create :as interactor]
   [publicator.factories :as factories]
   [publicator.fixtures :as fixtures]
   [publicator.interactors.services.user-session :as user-session]
   [ring.mock.request :as mock.request]
   [ring.util.http-predicates :as util.http-predicates]
   [form-ujs.ring]
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [clojure.spec.gen.alpha :as sgen]))

(t/use-fixtures :each fixtures/fake-bindings)

(t/deftest form
  (let [handler (handler/build)
        user    (factories/create-user)
        _       (user-session/log-in! user)
        req     (mock.request/request :get "/posts-new")
        resp    (handler req)]
    (t/is (util.http-predicates/ok? resp))))

(t/deftest handler
  (let [handler (handler/build)
        user    (factories/create-user)
        _       (user-session/log-in! user)
        params  (sgen/generate (s/gen ::interactor/params))
        req     (form-ujs.ring/data->request :post "/posts" params)
        resp    (handler req)]
    (t/is (form-ujs.ring/successful-response? resp))))
