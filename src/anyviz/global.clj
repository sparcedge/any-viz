(ns anyviz.global)

(def display-name (atom "AnyViz"))
(def turbinedb-url (atom "http://localhost:8080"))
(def server-port (atom 9000))

(defn update-atom [atom value]
  (if value (reset! atom value)))

(defn initialize-atoms [conf]
  (update-atom display-name (or (:display-name conf) "AnyViz"))
  (update-atom server-port (or (:server-port conf) 9000))
  (update-atom turbinedb-url (or (:turbinedb-url conf) "http://localhost:8080")))