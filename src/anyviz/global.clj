(ns anyviz.global)

(def display-name (atom "AnyViz"))
(def turbinedb-url (atom "http://localhost:8080"))
(def turbinedb-database (atom "concert"))
(def turbinedb-collection (atom "events"))
(def turbinedb-segments (atom []))
(def server-port (atom 9000))

(defn update-atom [atom value]
  (if value (reset! atom value)))

(defn initialize-atoms [conf]
  (update-atom display-name (or (:display-name conf) "AnyViz"))
  (update-atom server-port (or (:server-port conf) 9000))
  (update-atom turbinedb-url (or (:turbinedb-url conf) "http://localhost:8080"))
  (update-atom turbinedb-database (or (:turbinedb-database conf) "concert"))
  (update-atom turbinedb-collection (or (:turbinedb-collection conf) "energy"))
  (update-atom turbinedb-segments (or (:turbinedb-segments conf) [])))