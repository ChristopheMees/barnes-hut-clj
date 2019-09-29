(ns barnes-hut.model.boundary)

(defn width [{:keys [minx maxx]}]
  (- maxx minx))

(defn height [{:keys [miny maxy]}]
  (- maxy miny))

(defn size [boundary]
  (max (width boundary) (height boundary)))

(defn coord [{:keys [minx maxx miny maxy]}]
  [[minx miny] [maxx maxy]])

(defn quad-split [boundary]
  (let [[[tlx tly] [brx bry]] (coord boundary)
        halfx (+ tlx (/ (- brx tlx) 2))
        halfy (+ tly (/ (- bry tly) 2))]
    [{:minx tlx :miny tly :maxx halfx :maxy halfy}
     {:minx halfx :miny tly :maxx brx :maxy halfy}
     {:minx tlx :miny halfy :maxx halfx :maxy bry}
     {:minx halfx :miny halfy :maxx brx :maxy bry}]))

(defn inbound? [{:keys [minx maxx miny maxy]} {:keys [x y]}]
  (not (or (< x minx) (> x maxx) (< y miny) (> y maxy))))
