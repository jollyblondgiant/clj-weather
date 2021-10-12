(defproject compokedex "0.1.0-SNAPSHOT"
  :description "An http server that makes an api call and returns a parsed result"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"] ;; parses url params
                 [ring/ring-defaults "0.3.2"] ;;handles routing
                 [clj-http "3.12.3"] ;;makes calls to apis
                 [org.clojure/data.json "2.4.0"] ;;parse api data
                 ]
  :plugins [[lein-ring "0.12.5"]] ;;handles routing
  :ring {:handler compokedex.handler/app} ;;fires the app command
  )
