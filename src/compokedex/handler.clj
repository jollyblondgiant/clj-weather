(ns compokedex.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defonce api-key "9c4ea6d46adce936cc13c285b9aaea03")

(def alert-codes
  {701 "Mist"
   711 "Smoke"
   721 "Haze"
   731 "Dust"
   741 "Fog"
   751 "Sand"
   761 "Dust"
   762 "Ash"
   771 "Squall"
   781 "Tornado"
   })

(defn parse-weather
  ""
  [{{temp :temp
     [{weather :description
       status :id}] :weather} :current}]
  (let [temp (cond
               (< temp 20) "Chilly"
               (< temp 40) "Brisk"
               (> temp 80) "Warm"
               (> temp 90) "Hot"
               :else "Nice")
        res {:temp temp
             :weather weather}]
    (if-let [alert (get alert-codes status)]
      (assoc res :alerts alert)
      res)
    )
  )

(defn get-weather-at
  ""
  [lat lng]
  (let [api "https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&appid=%s&units=imperial"]
    (-> (format api lat lng api-key)
        client/get
        :body
        (json/read-str :key-fn keyword)
        parse-weather)
    ))

(defroutes app-routes
  (GET "/" [] "try appending /lat/lng to the url eg /0/0")
  (GET "/:lat/:lng" [lat lng] (json/write-str (get-weather-at lat lng)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
