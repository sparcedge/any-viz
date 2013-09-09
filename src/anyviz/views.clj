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
    [:link {:href "/static/css/anyviz.css" :rel "stylesheet" :media "screen"}]
    [:script {:src "/static/js/query-builder.js"}]
    [:script {:src "/static/js/moment.min.js"}]
    extras])

(defn nav-bar []
  [:div.navbar.navbar-default {:role "navigation"}
    [:a.navbar-brand {:href "#"} @g/display-name]
    [:ul.nav.navbar-nav]])

(def match-operators ["=","!=","<","<=",">",">="])
(def group-times ["year","month","day","hour","minute"])
(def reduce-operators ["count","min","max","sum","avg","stdev"])

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

(defn wg [el]
  [:div.col-lg-2 el])

(defn el-header [txt]
  [:h3 {:style "float: none; margin-left: auto; margin-right: auto;"} txt])

(defn query-builder [db coll segments]
  (html
    [:html
      (common-head)
      [:body
        (nav-bar)
        (breadcrumbs [["home" "/"] (linkify-db db) (linkify-coll db coll)])
        [:div.container
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
            [:button#go-btn.btn.btn-primary {:type "button"} "Run Query"]]]
        [:div {:style "text-align: center;"}
          (el-header "Graph")
          [:div#dynamic-graph {:style "width 100%; height:400px;"}]
          (el-header "Query")
          [:pre#query {:style "text-align: left;"}]
          (el-header "Results")
          [:pre#results {:style "text-align: left;"}]]]]))

(defn instance-view [dbs]
  (html
    [:html
      (common-head)
      [:body
        (nav-bar)
        (breadcrumbs [["home" "/"]])
        [:h3 {:style "text-align: center;"} "Select a Database"]
        [:div.container
          (linked-items (map linkify-db dbs))]]]))

(defn database-view [db colls]
  (html
    [:html
      (common-head)
      [:body
        (nav-bar)
        (breadcrumbs [["home" "/"] (linkify-db db)])
        [:h3 {:style "text-align: center;"} "Select a Collection"]
        [:div.container
          (linked-items (map #(linkify-coll db %) colls))]]]))
