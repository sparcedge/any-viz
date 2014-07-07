(ns anyviz.global)

(def brand-name (atom "AnyViz"))
(def company-name (atom "SPARC"))
(def turbinedb-url (atom "http://localhost:8080"))
(def server-port (atom 9000))

(defn update-atom [atom value]
  (if value (reset! atom value)))

(defn initialize-atoms [conf]
  (update-atom brand-name (or (:brand-name conf) "AnyViz"))
  (update-atom company-name (or (:company-name conf) "SPARC"))
  (update-atom server-port (or (:server-port conf) 9000))
  (update-atom turbinedb-url (or (:turbinedb-url conf) "http://localhost:8080")))