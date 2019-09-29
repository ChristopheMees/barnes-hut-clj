(ns barnes-hut.galaxy
  (:require [barnes-hut.constants :refer [gee]])
  (:import (java.util Random)))

(def random (new Random 213))

(defn mapper [{:keys [blackHoleM maxradius totalM cubmaxradius cx cy sx sy]}]
  (fn [x]
    (if (zero? x)
      {:mass blackHoleM :x cx :y cy :xSpeed sx :ySpeed sy}
      (let [angle (* (.nextFloat random) 2 Math/PI)
            radius (+ 25 (* maxradius (.nextFloat random)))
            starx (+ cx (* radius (Math/sin angle)))
            stary (+ cy (* radius (Math/cos angle)))
            speed (Math/sqrt (+ (/ (* gee blackHoleM) radius) (/ (* gee totalM radius radius) cubmaxradius)))
            starspeedx (+ sx (* speed (Math/sin (+ angle (/ Math/PI 2)))))
            starspeedy (+ sy (* speed (Math/cos (+ angle (/ Math/PI 2)))))
            starmass (inc (.nextFloat random))]
        {:mass starmass :x starx :y stary :xSpeed starspeedx :ySpeed starspeedy}
        ))))

(defn gen-galaxy [{:keys [num maxradius cx cy sx sy]}]
  (map (mapper {:blackHoleM   num
                :maxradius    maxradius
                :totalM       (* 1.5 num)
                :cubmaxradius (* maxradius)
                :cx           cx
                :cy           cy
                :sx           sx
                :sy           sy})
       (range num)))
