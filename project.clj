(defproject anyviz "0.1.0-SNAPSHOT"
  :description "TurbineDB Visual Query Builder"
  :url "http://turbinedb.com"
  :main anyviz.core
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [http-kit "2.2.0"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.5"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-devel "1.1.8"]
                 [clj-http "2.3.0"]
                 [cheshire "5.7.0"]
                 [tentacles "0.3.0"]
                 [clj-time "0.13.0"]])