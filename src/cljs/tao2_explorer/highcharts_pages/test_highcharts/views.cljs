(ns tao2-explorer.highcharts-pages.test-highcharts.views
  (:require [reagent.core :as reagent]
            [reagent.ratom :as ratom]
            [re-frame.core :refer [subscribe dispatch]]
            [cljs-react-material-ui.core :as ui]
            [cljs-react-material-ui.reagent :as rui]
            [tao2-explorer.highcharts-pages.test-highcharts.subs]
            [tao2-explorer.highcharts-pages.test-highcharts.events]
            [cljsjs.highcharts]
            [cljsjs.handsontable]))


;(def chart-config
;  {
;   :title    {:text "Historic World Population by Region"}
;   :subtitle {:text "Source: Wikipedia.org"}
;   :xAxis    {:categories ["Africa" "America" "Asia" "Europe" "Oceania"]
;              :title      {:text nil}}
;   :yAxis    {:min       0
;              :title     {:text  "Population (millions)"
;                          :align "high"}
;              :labels    {:overflow "justify"}
;              :plotLines [{
;                           :value 0
;                           :width 1}]}
;
;
;   :tooltip  {:valueSuffix " millions"}
;   :legend   {:layout        "vertical"
;              :align         "left"
;              :verticalAlign "middle"
;              :shadow        false}
;
;   :credits  {:enabled false}
;   :series   [{:name "Year 1800"
;               :data [107 31 635 203 2]}
;              {:name "Year 1900"
;               :data [133 156 947 408 6]}
;              {:name "Year 2008"
;               :data [973 914 4054 732 34]}]})

;(def table-config
;  {
;   :data        [
;                 ["" "Kia" "Nissan" "Toyota" "Honda"]
;                 ["2008" 10 11 12 13]
;                 ["2009" 20 11 14 13]
;                 ["2010" 30 15 12 13]
;                 ]
;   :rowHeaders  false
;   :colHeaders  false
;   :contextMenu true
;   })

;(defn foo
;  []
;  println "hello")


(defonce high-ret-data
  (reagent/atom
   { :title    {:text ""}
    :subtitle {:text ""} ;; :chart {:type "area"}
    :xAxis    {:categories
               ["Africa" "America" "Asia" "Europe" "Oceania"]
               :title      {:text nil}}
    :yAxis    {:min       0
               :title     {:text  "金额 (元)"
                           :align "high"}
               :labels    {:overflow "justify"}
               :plotLines [{
                            :value 0
                            :width 1}]}
    :tooltip  {:valueSuffix " 元"}
    :legend   {:layout        "vertical"
               :align         "left"
               :verticalAlign "middle"
               :shadow        false}
    :credits  {:enabled false}}) )

(defn gen-chart-config-handson [tableconfig]
  (let [tabledata (:data tableconfig)
        categories (vec (rest (get tabledata 0)))
        data1 (assoc {} :name (str (first (get tabledata 1))) :data (vec (rest (get tabledata 1))))
        data2 (assoc {} :name (str (first (get tabledata 2))) :data (vec (rest (get tabledata 2))))
        mydata (conj [] data1 data2)]
    (swap! high-ret-data assoc-in [:xAxis :categories] categories)
    (swap! high-ret-data assoc-in [:series] mydata))
  high-ret-data)

(defn sampleTable-render []
  [:div {:style {:min-width "310px" :max-width "800px" :margin "0 auto"}}])

(defn sampleTable-did-mount [this]
  (let [[_ tableconfig] (reagent/argv this)
        tableconfigext (assoc-in tableconfig [:afterChange] #(dispatch [:test-highcharts/set-tablevalue %]))]
    (do
      (js/Handsontable (reagent/dom-node this) (clj->js tableconfigext)))))

(defn sampleTable [tableconfig]
  (reagent/create-class {:reagent-render      sampleTable-render
                         :component-did-mount sampleTable-did-mount}))
(defn sampleHighchart-render []
  [:div {:style {:min-width "310px" :max-width "800px" :margin "0 auto"}}])

(defn sampleHighchart-did-mount [this]
  (let [[_ tableconfig] (reagent/argv this)
        my-chart-config (gen-chart-config-handson tableconfig)]
    (js/Highcharts.Chart. (reagent/dom-node this) (clj->js @my-chart-config))))

(defn sampleHighchart-did-update [this]
  (let [[_ tableconfig] (reagent/argv this)
        my-chart-config (gen-chart-config-handson tableconfig)]
    (do
      (js/Highcharts.Chart. (reagent/dom-node this) (clj->js @my-chart-config)))))

;; 如果你不知道怎么做时那就快速lambda化
(defn sampleHighchart [tableconfig]
  (reagent/create-class {:reagent-render      sampleHighchart-render
                         :component-did-mount sampleHighchart-did-mount
                         :component-did-update sampleHighchart-did-update}))

(defn test-highcharts-page
  []
  (let [chartname (subscribe [:test-highcharts/chartname])
        tableconfig (subscribe [:test-highcharts/tableconfig])]
    (fn []
      [:div
       [:div.row {:style {:margin-bottom "10px"}}
        [:div.col-xs-6
         [rui/text-field {:name      "highchart-name"
                          :hint-text "Steve Highchart Name"
                          :on-change #(dispatch [:test-highcharts/set-chartname
                                                 (.-value (.-target %))])}]]]
       [:div.row
        [:div.col-xs-12
         [rui/card {:style {:min-height "100%"}}
          [rui/card-title {:title @chartname
                           :style {:background-color (ui/color :grey300)}}]
          [:div {:style {:padding "10px"}}
           [sampleTable @tableconfig]]]]]
       [:div.row
        [:div.col-xs-12
         [rui/card {:style {:min-height "100%"}}
          [rui/card-title {:title @chartname
                           :style {:background-color (ui/color :grey300)}}]
          [:div {:style {:padding "10px"}}
           [sampleHighchart @tableconfig]]]]]])))



