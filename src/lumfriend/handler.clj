(ns lumfriend.handler
  (:require [compojure.core :refer [defroutes]]
            [compojure.handler :as handler]
            [lumfriend.routes.home :refer [home-routes]]
            [lumfriend.middleware :as middleware]
            [noir.util.middleware :refer [app-handler]]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.rotor :as rotor]
            [selmer.parser :as parser]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [environ.core :refer [env]]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/appender-fn})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "lumfriend.log" :max-size (* 512 1024) :backlog 10})

  (if (env :dev) (parser/cache-off!))
  (timbre/info "lumfriend started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "lumfriend is shutting down..."))

; Dummy database of users
(def users {"admin" {:username "admin"
                    :password (creds/hash-bcrypt "password")
                    :roles #{::admin}}
            "dave" {:username "user"
                    :password (creds/hash-bcrypt "password")
                    :roles #{::user}}})

(def app (app-handler
          ;; add your application routes here
          [home-routes app-routes]
          ;; add custom middleware here
          :middleware [middleware/template-error-page
                       middleware/log-request]
          ;; add access rules here
          :access-rules []
          ;; serialize/deserialize the following data formats
          ;; available formats:
          ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
          :formats [:json-kw :edn]

          ;Friend - Is this the right Place to configure friend?
          ;If so, how do I pass the other routs? [Home-routes app-routes ...] from line 56?
          :page (handler/site
            (friend/authenticate home-routes {
              :credentials-fn (partial creds/bcrypt-credential-fn users)
              :workflows [(workflows/interactive-form)]}))

          ))