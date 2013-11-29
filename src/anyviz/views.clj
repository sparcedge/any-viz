(ns anyviz.views
  (:require [hiccup.core :refer :all]
            [anyviz.global :as g]))

(defn common-head [& extras]
  [:head
    [:title @g/brand-name]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:link {:href "/static/bootstrap/css/bootstrap.min.css" :rel "stylesheet" :media "screen"}]
    [:script {:src "http://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"}]
    [:script {:src "/static/bootstrap/js/bootstrap.min.js"}]
    [:script {:src "http://code.highcharts.com/highcharts.js"}]
    [:script {:src "http://code.highcharts.com/modules/exporting.js"}]
    [:link {:href "/static/bootstrap/css/lavish-theme.css" :rel "stylesheet" :media "screen"}]
    [:link {:href "/static/css/anyviz.css" :rel "stylesheet" :media "screen"}]
    [:script {:src "/static/js/query-builder.js"}]
    [:script {:src "/static/js/turbine.js"}]
    [:script {:src "/static/js/moment.min.js"}]
    extras])

(defn nav-bar []
  [:div.navbar.navbar-inverse.navbar-fixed-top {:role "navigation"}
    [:div.container
      [:a.navbar-brand {:href "#"} @g/brand-name]
      [:ul.nav.navbar-nav]]])

(defn jumbotron [title sub-title]
  [:div.jumbotron
    [:h1 title]
    [:p sub-title]])

(defn common-footer []
  [:div#footer.footer
    [:div.text-muted "Copyright &copy; 2013 " @g/brand-name ". All Rights Reserved"]])

(def match-operators ["=","!=","<","<=",">",">="])
(def group-times ["year","month","day","hour","minute"])
(def reduce-operators ["count","min","max","sum","avg","harmonicmean","stdev","variance","range"])
(def date-periods ["all time","last year","last 6 months","last 30 days","last 7 days","last day","last hour"])
(def graph-types ["spline","column","line","area","bar","areaspline","scatter"])
(def bs-columns ["col-sm-1","col-sm-2","col-sm-3","col-sm-4","col-sm-5","col-sm-6","col-sm-7","col-sm-8","col-sm-9","col-sm-10","col-sm-11","col-sm-12"])

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

(defn label [label target]
  [:label.col-sm-2.control-label {:for target} label])

(defn group [el & extras]
  [:div.form-group el extras])

(defn ctl 
  ([el] (ctl el 4))
  ([el col] 
    [:div {:class (nth bs-columns (- col 1))} el]))

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
                (group
                  (label "Over the range" "date-period")
                  (ctl (dropdown date-periods "date-period"))
                  (label "Group time by" "group-times")
                  (ctl (dropdown group-times "group-times")))
                (group
                  (label "Match where" "match-entities")
                  (ctl (dropdown segments "none" "match-entities"))
                  (ctl (dropdown match-operators "none" "match-ops") 2)
                  (ctl (input "value")))
                (group
                  (label "Group results by" "group-entities")
                  (ctl (dropdown segments "none" "group-entities")))
                (group
                  (label "Aggregate" "reduce-entities")
                  (ctl (dropdown segments "reduce-entities"))
                  (label "Over the statistic" "reduce-ops")
                  (ctl (dropdown reduce-operators "reduce-ops")))
                (group
                  (label "Display results as" "graph-type")
                  (ctl (dropdown graph-types "graph-type")))
                [:div.form-group
                  [:div.col-sm-10.col-sm-offset-2
                    [:button#go-btn.btn.btn-primary {:type "button"} "Add Series"]
                    "&nbsp;"
                    [:button#clear-btn.btn.btn-default {:type "button"} "Clear Graph"]]]]]]

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
              [:pre#results {:style "text-align: left;"}]]]]
        (common-footer)]]))

(defn instance-view [dbs]
  (html
    [:html
      (common-head)
      [:body
        (nav-bar)
        [:div.container
          (breadcrumbs [["home" "/"]])
          (jumbotron (str "Welcome to " @g/brand-name "!") "Step 1 is to get started please select a database from the list below")
          [:div.panel.panel-primary
            [:div.panel-heading
              [:h3.panel-title "Select a Database"]]
            [:div.panel-body
              (linked-items (map linkify-db dbs))]]]
        (common-footer)]]))

(defn database-view [db colls]
  (html
    [:html
      (common-head)
      [:body
        (nav-bar)
        [:div.container
          (breadcrumbs [["home" "/"] (linkify-db db)])
          (jumbotron "Almost there!" "Step 2 is to select a collection from the list below")
          [:div.panel.panel-primary
            [:div.panel-heading
              [:h3.panel-title "Select a Collection"]]
            [:div.panel-body
              (linked-items (map #(linkify-coll db %) colls))]]]
        (common-footer)]]))




