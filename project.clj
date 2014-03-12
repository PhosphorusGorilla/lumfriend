(defproject lumfriend "0.1.0"
  :description "Friend to be friends with LuminusWeb"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lib-noir "0.8.1"]
                 [compojure "1.1.6"]
                 [ring-server "0.3.1"]
                 [selmer "0.6.1"]
                 [com.taoensso/timbre "3.0.0"]
                 [com.taoensso/tower "2.0.1"]
                 [markdown-clj "0.9.41"]
                 [environ "0.4.0"]
                 [com.cemerick/friend "0.2.0"]
                 ; Necessary to avoid the "unable to load org.core.cache" error.
                 ; Looks like the clojure.core.cache/through function references its own clojure.core.cache namespace before its loaded 
                 ; Forcing lein to load that namespace first seems to bypass that problem somehow.
                 [org.clojure/core.cache "0.6.3"]]

  :repl-options {:init-ns lumfriend.repl}
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "0.4.0"]]
  :ring {:handler lumfriend.handler/app
         :init    lumfriend.handler/init
         :destroy lumfriend.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.2.1"]]
         :env {:dev true}}}
  :min-lein-version "2.0.0")