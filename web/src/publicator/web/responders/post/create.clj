(ns publicator.web.responders.post.create
  (:require
   [publicator.use-cases.interactors.post.create :as interactor]
   [publicator.web.responders.base :as responders.base]
   [publicator.web.responses :as responses]
   [publicator.web.forms.post.params :as form]))

(defmethod responders.base/result->resp ::interactor/initial-params [[_ params]]
  (let [form (form/build-create params)]
    (responses/render-form form)))

(derive ::interactor/processed ::responders.base/redirect-to-root)
(derive ::interactor/invalid-params ::responders.base/invalid-params)
(derive ::interactor/logged-out ::responders.base/forbidden)
