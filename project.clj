(defproject compokedex "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"] ;; parses url params
                 [ring/ring-defaults "0.3.2"] ;;handles routing
                 [http-kit "2.5.3"] ;; runs server
                 [clj-http "3.12.3"] ;;makes calls to apis
                 [org.clojure/data.json "2.4.0"] ;;parse api data
                 ]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler compokedex.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
