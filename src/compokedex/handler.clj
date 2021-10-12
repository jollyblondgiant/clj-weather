(ns compokedex.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults]]))

(defonce api-key "9c4ea6d46adce936cc13c285b9aaea03")
;;^^the key I used to set up api access

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
#_"
Since 'weather alerts' did not appear to be covered by the api,
I understood this to mean 'terrible weather events happening at
the given location' and  found most of those events to be in the
700 range 'Atmospheric events' https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
"



(defn parse-weather
  "destructures the body of a request to format it into a concise
  response that includes the temperature, weather condition,
  and weather alerts, if any"
  [{{temp :temp
     [{weather :description
       status :id}] :weather} :current}]
  ;;^^destrutctures the incoming response body for relevant data
  (let [temp (cond
               (< temp 20) "Chilly"
               (< temp 40) "Brisk"
               (> temp 80) "Warm"
               (> temp 90) "Hot"
               :else "Nice")
        ;;^^ my personal  taste in room temperatures
        res {:temp temp
             :weather weather}]
    ;;^^ the desired, formatted response
    (if-let [alert (get alert-codes status)]
      ;;^^ this binds the value of alert codes only if that code exists as a key in alert codes; otherwise, it returns the response above
      (assoc res :alerts alert)
      res)
    )
  )

(defn get-weather-at
  "receives two params that represent latitude and longitude,
  extracts the response body, converts the json to a hash-map,
  and parses it through parse-weather
  "
  [lat lng]
  (-> (str "https://api.openweathermap.org/data/2.5"
           "/onecall?lat=%s&lon=%s&appid=%s&units=imperial")
      ;;^^ could be one string, but formatted for good ole vgrep
      (format lat lng api-key) ;; string interpolation of params
      client/get ;; make the api call.
      :body ;; parse into first layer of response
      (json/read-str :key-fn keyword) ;; parse response body into hash-map
      parse-weather)
  )

(defroutes app-routes
  ;;^^ exposes the endpoints of the server using compojure.core
  (GET "/" [] "try appending /lat/lng to the url eg /0/0")
  (GET "/:lat/:lng" [lat lng]
       (json/write-str (get-weather-at lat lng)))
  (route/not-found "Not Found"))

(def app
  ;; serves up endpoints defined in app-routes so that we can
  ;; capture params as url inputs
  (wrap-defaults app-routes {:params  {:keywordize true}}))
