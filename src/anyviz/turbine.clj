(ns anyviz.turbine
  (:require [clj-http.client :as client]
            [clojure.string :as str]
            [cheshire.core :as json]
            [anyviz.global :as g]
            [clj-time.coerce :refer :all]))

(defn coll-url []
  (str @g/turbinedb-url "/db/" @g/turbinedb-database "/" @g/turbinedb-collection))

(defn query [q]
  (let [q-json (json/generate-string q)
        url (coll-url)
        res (client/get url {:query-params {"q" q-json}})
        body (:body res)]
    (json/parse-string body true)))

(defn get-first [key results]
  (-> results first :data first :data first key))

(defn create-query [matches groups reduces start end]
  (merge
    {:match matches :group groups :reduce reduces}
    (when start {:start start})
    (when end {:end end})))

(defn valid-reducer? [[k v]]
  (and (not (nil? k)) (not (nil? v))))

(defn convert-reducer [[k v]]
  {(str k "-" v) {v k}})

(defn get-reducers [params]
  (when-let [reducers (:reducers params)]
    (->> (str/split reducers #",")
         (map #(str/split % #":"))
         (filter valid-reducer?)
         (map convert-reducer))))

(defn convert-group [group]
  (if (contains? #{"minute" "hour" "day" "month" "year"} group) 
    {"duration" group}
    {"segment" group}))

(defn get-groups [params]
  (when-let [groups (:groups params)]
    (->> (str/split groups #",")
         (map convert-group)
         (remove nil?))))

(defn parse-double [s]
  (Double/parseDouble s))

(defn convert-match [[s m v]]
  (if (not= m "eq")
    {s {m (parse-double v)}}
    {s {m v}}))

(defn invalid-match? [[s m v]]
  (or (nil? s) (nil? m) (nil? v)))

(defn get-matches [params]
  (when-let [matches (:matches params)]
    (->> (str/split matches #",")
         (map #(str/split % #":"))
         (remove invalid-match?)
         (map convert-match))))

(defn long-or-nil [str]
  (when str
    (Long/parseLong str)))

(defn create-query-from-params [params]
  (let [matches (get-matches params)
        groups (get-groups params)
        reducers (get-reducers params)
        start (-> params :start long-or-nil)
        end (-> params :end long-or-nil)]
    (create-query matches groups reducers start end)))