(ns anyviz.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [org.httpkit.server :refer :all]
            [ring.util.response :refer :all]
            [compojure.core :refer [defroutes GET POST]]
            [ring.middleware.reload :as reload]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [cheshire.core :as json]
            [anyviz.global :as g]
            [anyviz.turbine :as t]
            [anyviz.views :as v]))

(def resource-conf (-> "config.json" io/resource))

(defn read-conf [file]
  (json/parse-string (slurp (or file resource-conf)) true))

(defn query-turbine [params]
  (let [query (t/create-query-from-params params)
        db (:db query)
        coll (:coll query)
        results (t/query query db coll)]
    {:query query :results results}))

(defroutes routes
  (GET "/alo" [] "alo guvna")
  (GET "/query" {params :params} (-> params query-turbine json/generate-string))
  (GET "/" [] (v/instance-view (t/get-databases)))
  (GET "/db/:db" [db] (v/database-view db (t/get-collections db)))
  (GET "/db/:db/:coll" [db coll] (v/query-builder db coll (t/get-segments db coll)))
  (route/resources "/static/"))

(defn app-routes [{mode :mode}]
  (if (= mode "prod")
    (handler/site routes)
    (-> #'routes handler/site reload/wrap-reload)))

(defn -main [& [conf-file]]
  (let [conf (read-conf conf-file)
        app (app-routes conf)]
    (g/initialize-atoms conf)
    (run-server app {:port @g/server-port :join? false})))