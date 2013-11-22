(ns anyviz.global)

(def brand-name (atom "AnyViz"))
(def turbinedb-url (atom "http://localhost:8080"))
(def server-port (atom 9000))

(defn update-atom [atom value]
  (if value (reset! atom value)))

(defn initialize-atoms [conf]
  (update-atom brand-name (or (:brand-name conf) "AnyViz"))
  (update-atom server-port (or (:server-port conf) 9000))
  (update-atom turbinedb-url (or (:turbinedb-url conf) "http://localhost:8080")))