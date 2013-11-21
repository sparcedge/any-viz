(ns anyviz.views
  (:require [hiccup.core :refer :all]
            [anyviz.global :as g]))

(defn common-head [& extras]
  [:head
    [:title @g/display-name]
    [:link {:href "/static/bootstrap/css/bootstrap.min.css" :rel "stylesheet" :media "screen"}]
    [:script {:src "http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"}]
    [:script {:src "/static/bootstrap/js/bootstrap.min.js"}]
    [:script {:src "http://code.highcharts.com/highcharts.js"}]
    [:link {:href "/static/bootstrap/css/lavish-theme.css" :rel "stylesheet" :media "screen"}]
    [:link {:href "/static/css/anyviz.css" :rel "stylesheet" :media "screen"}]
    [:script {:src "/static/js/query-builder.js"}]
    [:script {:src "/static/js/turbine.js"}]
    [:script {:src "/static/js/moment.min.js"}]
    extras])

(defn nav-bar []
  [:div.navbar.navbar-inverse.navbar-fixed-top {:role "navigation"}
    [:div.container
      [:a.navbar-brand {:href "#"} @g/display-name]
      [:ul.nav.navbar-nav]]])

(defn common-footer []
  [:div#footer.footer
    [:div.text-muted "Copyright &copy; 2013, All Rights Reserved"] ])

(def match-operators ["=","!=","<","<=",">",">="])
(def group-times ["year","month","day","hour","minute"])
(def reduce-operators ["count","min","max","sum","avg","stdev"])
(def date-periods ["all time","last year","last 6 months","last 30 days","last 7 days","last day","last hour"])
(def graph-types ["spline","column","line","area","bar","areaspline","scatter"])

(defn dropdown-item [item]
  [:option {:value item} item])

(defn dropdown-item-default [item]
  [:option {:value item :selected "selected"} item])

(defn dropdown
  ([items id] (dropdown (rest items) (first items) id))
  ([items default id]
    [:select.query-select.form-control {:id id}
      (cons 
        (dropdown-item-default default)
        (map dropdown-item items))]))

(defn breadcrumb [[name url]]
  [:li
    [:a {:href url} name]])

(defn breadcrumbs [crumbs]
  [:ol.breadcrumb
    (map breadcrumb crumbs)])

(defn linked-item [[name url]]
  [:a.list-group-item {:href url} name])

(defn linked-items [litems]
  [:div.list-group
    (map linked-item litems)])

(defn linkify-db [db]
  [db (str "/db/" db)])

(defn linkify-coll [db coll]
  [coll (str "/db/" db  "/" coll)])

(defn input [label]
  [:input#match-val.query-input.form-control {:type "text" :placeholder label}])

(defn wg4 [el & extras]
  [:div.col-sm-4 el])

(defn wg3 [el & extras]
  [:div.col-sm-3 el])

(defn wg2 [el & extras]
  [:div.col-sm-2 el])

(defn wg10 [el & extras]
  [:div.col-sm-10 el])

(defn wg5 [el & extras]
  [:div.col-sm-5 el])

(defn el-header [txt]
  [:h3 {:style "float: none; margin-left: auto; margin-right: auto;"} txt])

(defn query-builder [db coll segments]
  (html
    [:html
      (common-head)
      [:body
        (nav-bar)
        [:div.container
          (breadcrumbs [["home" "/"] (linkify-db db) (linkify-coll db coll)])
          [:div.panel.panel-primary
            
            [:div.panel-heading
              [:h3.panel-title "Builder"]]

            [:div.panel-body
              [:form.form-horizontal {:role "form"}
                [:div.form-group
                  [:label.col-sm-2.control-label {:for "date-period"} "Over the range"]
                  (wg4 (dropdown date-periods "date-period"))
                  [:label.col-sm-2.control-label {:for "group-times"} "Group time by"]
                  (wg4 (dropdown group-times "group-times"))]

                [:div.form-group
                  [:label.col-sm-2.control-label {:for "match-entities"} "Match where"]
                  (wg4 (dropdown segments "none" "match-entities"))
                  (wg2 (dropdown match-operators "none" "match-ops"))
                  (wg4 (input "value"))]

                [:div.form-group
                  [:label.col-sm-2.control-label {:for "group-entities"} "Group results by"]
                  (wg10 (dropdown segments "none" "group-entities"))]

                [:div.form-group
                  [:label.col-sm-2.control-label {:for "reduce-entities"} "Aggregate"]
                  (wg4 (dropdown segments "reduce-entities"))
                  [:label.col-sm-2.control-label {:for "reduce-ops"} "Over the statistic"]
                  (wg4 (dropdown reduce-operators "reduce-ops"))]

                [:div.form-group
                  [:label.col-sm-2.control-label {:for "graph-type"} "Display results as"]
                  (wg10 (dropdown graph-types "graph-type"))]

                [:div.form-group
                  [:div.col-sm-10.col-sm-offset-2
                    [:button#go-btn.btn.btn-primary {:type "button"} "Run Query"]
                    "&nbsp;"
                    [:button#clear-btn.btn.btn-default {:type "button"} "Clear Query"]]]]]]

          [:ul#query-tabs.nav.nav-tabs.nav-pills
            [:li.active [:a {:href "#graph" :data-toggle "tab"} "Graph"]]
            [:li [:a {:href "#rawquery" :data-toggle "tab"} "Query"]]
            [:li [:a {:href "#queryresults" :data-toggle "tab"} "Results"]]]

          [:div.tab-content 
            [:div#graph.tab-pane.active
              [:div#dynamic-graph {:style "width 100%; height:600px;"}]]
            [:div#rawquery.tab-pane
              [:pre#query {:style "text-align: left;"}]]
            [:div#queryresults.tab-pane
              [:pre#results {:style "text-align: left;"}]]]]]
      (common-footer)]))

(defn instance-view [dbs]
  (html
    [:html
      (common-head)
      [:body
        (nav-bar)
        [:div.container
          (breadcrumbs [["home" "/"]])
          [:div.panel.panel-primary
            [:div.panel-heading
              [:h3.panel-title "Select a Database"]]
            [:div.panel-body
              (linked-items (map linkify-db dbs))]]]]
      (common-footer)]))

(defn database-view [db colls]
  (html
    [:html
      (common-head)
      [:body
        (nav-bar)
        [:div.container
          (breadcrumbs [["home" "/"] (linkify-db db)])
          [:div.panel.panel-primary
            [:div.panel-heading
              [:h3.panel-title "Select a Collection"]]
            [:div.panel-body
              (linked-items (map #(linkify-coll db %) colls))]]]]
      (common-footer)]))