(ns publicator.web.responders.post.update
  (:require
   [publicator.use-cases.interactors.post.update :as interactor]
   [publicator.web.responders.base :as responders.base]
   [publicator.web.responses :as responses]
   [publicator.web.forms.post.params :as form]))

(defmethod responders.base/result->resp ::interactor/initial-params [[_ post params]]
  (let [form (form/build-update (:id post) params)]
    (responses/render-form form)))

(derive ::interactor/processed ::responders.base/redirect-to-root)
(derive ::interactor/invalid-params ::responders.base/invalid-params)
(derive ::interactor/logged-out ::responders.base/forbidden)
(derive ::interactor/not-authorized ::responders.base/forbidden)
(derive ::interactor/not-found ::responders.base/not-found)
