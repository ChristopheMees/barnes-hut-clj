(ns barnes-hut.model.sector-matrix
  (:require [barnes-hut.model.boundary :as b]))

(defn ->sector-size [boundaries sector-precision]
  (/ (b/size boundaries) sector-precision))

(defn add [{:keys [boundaries sector-precision]  :as sector-matrix}
           {:keys [x y] :as body}]
  (let [sector-size (->sector-size boundaries sector-precision)
        row-coord (Math/round (/ (- x (:minx boundaries)) sector-size))
        col-coord (Math/round (/ (- y (:miny boundaries)) sector-size))
        mx (if (= row-coord sector-precision) (dec sector-precision) row-coord)
        my (if (= col-coord sector-precision) (dec sector-precision) col-coord)]
    (update sector-matrix
            :matrix
            (fn [m] (update m
                            {:x mx :y my}
                            (fn [col] (conj col body)))))))

