(ns lumfriend.routes.home
  (:use compojure.core)
  (:require [lumfriend.views.layout :as layout]
            [lumfriend.util :as util]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])))

(defn login-page []
  (layout/render "login.html"))

(defn about-page []
  (layout/render "about.html"))

(defn process-login
	[uname pwd]
	(if (and (= uname "vin") (= pwd "123"))
		(about-page)
		(str "INVALIDS")))

(defroutes home-routes
	(GET "/" request (friend/authorize #{::user} (about-page)))
  (GET "/login" [] (login-page))
  (POST "/login" [uname pwd] (process-login uname pwd))
  )
