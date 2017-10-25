(ns publicator.interactors.post.create
  (:require
   [publicator.interactors.services.user-session :as user-session]
   [publicator.interactors.abstractions.storage :as storage]
   [publicator.domain.post :as post]
   [better-cond.core :as b]
   [clojure.spec.alpha :as s]))

(s/def ::params (s/keys :req-un [::post/title]))

(defn- check-logged-in []
  (when (user-session/logged-out?)
    {:type ::logged-out}))

(defn- check-params [params]
  (when-let [exp (s/explain-data ::params params)]
    {:type         ::invalid-params
     :explain-data exp}))

(defn- create-post [params]
  (let [user   (user-session/user)
        params (assoc params :author-id (:id user))]
    (storage/tx-create (post/build params))))

(b/defnc initial-params []
  :let [err (check-logged-in)]
  (some? err) err
  {:type ::initial-params
   :initial-params {}})

(b/defnc process [params]
  :let [err (or
             (check-logged-in)
             (check-params params))]
  (some? err) err
  :let [post (create-post params)]
  {:type ::processed
   :post post})