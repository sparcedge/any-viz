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

(defn wg [el & extras]
  [:div.col-md-3 el])

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
              [:div.row
                (wg [:label "Match: "])
                (wg (dropdown segments "none" "match-entities"))
                (wg (dropdown match-operators "none" "match-ops"))
                (wg (input "value"))]
              [:div.row
                (wg [:label "Group (Segment)"])
                (wg (dropdown segments "none" "group-entities"))
                (wg [:label "Group (Time)"])
                (wg (dropdown group-times "group-times"))]
              [:div.row
                (wg [:label "Reduce"])
                (wg (dropdown segments "reduce-entities"))
                (wg (dropdown reduce-operators "reduce-ops"))]
              [:div.row
                (wg [:label "Period"])
                (wg (dropdown date-periods "date-period"))]
              [:div.row
                (wg [:label "Graph Type"])
                (wg (dropdown graph-types "graph-type"))] 
              [:div.row
                [:div.col-md-3 
                  [:button#go-btn.btn.btn-primary {:type "button"} "Run Query"]
                  [:button#clear-btn.btn.btn-default {:type "button"} "Clear Query"]]]]]

          [:ul#query-tabs.nav.nav-tabs.nav-pills
            [:li.active [:a {:href "#graph" :data-toggle "tab"} "Graph"]]
            [:li [:a {:href "#rawquery" :data-toggle "tab"} "Query"]]
            [:li [:a {:href "#queryresults" :data-toggle "tab"} "Results"]]]

          [:div.tab-content 
            [:div#graph.tab-pane.active
              [:div#dynamic-graph {:style "width 100%; height:325px;"}]]
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