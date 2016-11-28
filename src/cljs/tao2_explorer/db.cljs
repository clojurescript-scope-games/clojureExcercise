(ns tao2-explorer.db)

(def init-tableconfig { :data [["" "Kia" "Nissan" "Toyota" "Honda"]
                               ["2008" 321 32 345 352]
                               ["2009" 432 233 11 321]
                               ["2010" 1223 321 55 325]
                               ]
                       :rowHeaders  false
                       :colHeaders  false
                       :contextMenu false})

(def initial-db
  {:test-highcharts {:tableconfig init-tableconfig}})
