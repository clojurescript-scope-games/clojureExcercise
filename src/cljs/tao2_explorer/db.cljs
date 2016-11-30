(ns tao2-explorer.db)

(def init-tableconfig
  { :data [ (range 1 13)
           ["线下销售金额" 274, 305, 319, 170, 80, 330, 102, 116, 150, 191, 64, 329]
           ["线上销售金额" 261, 437, 348, 447, 355, 420, 254, 170, 180, 199, 419, 86]]
   :rowHeaders  false
   :colHeaders  false
   :contextMenu false})

(def initial-db
  {:test-highcharts {:tableconfig init-tableconfig}})
