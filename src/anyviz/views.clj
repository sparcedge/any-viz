(ns anyviz.views
  (:require [hiccup.core :refer :all]
            [anyviz.global :as g]))

(defn nav-bar [page]
  [:div.navbar.navbar-default {:role "navigation"}
    [:a.navbar-brand {:href "#"} @g/display-name]
    [:ul.nav.navbar-nav]])

(def match-operators ["=","!=","<","<=",">",">="])
(def group-times ["year","month","hour","day","minute"])
(def reduce-operators ["count","min","max","sum","avg","stdev"])

(defn dropdown-item [item]
  [:li {:role "presentation"}
    [:a {:role "menuitem" :tabindex "-1" :href "#"} item]])

(defn dropdown 
  ([items] (dropdown (rest items) (first items)))
  ([items default]
    [:div.dropdown
      [:ul.dropdown-menu {:role "menu" :aria-labelledby "dropdownMenu1"}
        (map dropdown-item (cons default items))]]))

(defn dropdown-item-simple [item]
  [:option {:value item} item])

(defn dropdown-item-simple-default [item]
  [:option {:value item :selected "selected"} item])

(defn dropdown-simple
  ([items id] (dropdown-simple (rest items) (first items) id))
  ([items default id]
    [:select.query-select.form-control {:id id}
      (cons 
        (dropdown-item-simple-default default)
        (map dropdown-item-simple items))]))

(defn input [label]
  [:input#match-val.query-input.form-control {:type "text" :placeholder label}])

(defn wg [el]
  [:div.col-lg-2 el])

(defn el-header [txt]
  [:h3 {:style "float: none; margin-left: auto; margin-right: auto;"} txt])

(defn query-builder []
  (html
    [:html
      [:head
        [:link {:href "bootstrap/css/bootstrap.min.css" :rel "stylesheet" :media "screen"}]
        [:script {:src "http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"}]
        [:script {:src "bootstrap/js/bootstrap.min.js"}]
        [:script {:src "http://code.highcharts.com/highcharts.js"}]
        [:link {:href "css/concertviz.css" :rel "stylesheet" :media "screen"}]
        [:script {:src "js/query-builder.js"}]
        [:script {:src "js/moment.min.js"}]]
      [:body
        (nav-bar :query-builder)
        [:div.container
          [:div.row
            (wg [:label "Match: "])
            (wg (dropdown-simple @g/turbinedb-segments "none" "match-entities"))
            (wg (dropdown-simple match-operators "none" "match-ops"))
            (wg (input "value"))]
          [:div.row
            (wg [:label "Group (Segment)"])
            (wg (dropdown-simple @g/turbinedb-segments "none" "group-entities"))
            (wg [:label "Group (Time)"])
            (wg (dropdown-simple group-times "group-times"))]
          [:div.row
            (wg [:label "Reduce"])
            (wg (dropdown-simple @g/turbinedb-segments "reduce-entities"))
            (wg (dropdown-simple reduce-operators "reduce-ops"))]
          [:div.row
            [:button#go-btn.btn.btn-primary {:type "button"} "Run Query"]]]
        [:div {:style "text-align: center;"}
          (el-header "Graph")
          [:div#dynamic-graph {:style "width 100%; height:400px;"}]
          (el-header "Query")
          [:pre#query {:style "text-align: left;"}]
          (el-header "Results")
          [:pre#results {:style "text-align: left;"}]]]]))