(ns barnes-hut.simulator
  (:require [barnes-hut.galaxy :refer [gen-galaxy]]
            [barnes-hut.state :refer [state]]
            [barnes-hut.model.sector-matrix :as m]
            [barnes-hut.constants :refer [sector-precision]]
            [barnes-hut.model.boundary :as b]))

(def total-bodies 25000)

(defn init-2-galaxies []
  (concat
    (gen-galaxy {:num       (/ total-bodies 8)
                 :maxradius 300
                 :cx        0
                 :cy        0
                 :sx        0
                 :sy        0})
    (gen-galaxy {:num       (* (/ total-bodies 8) 7)
                 :maxradius 350
                 :cx        -1800
                 :cy        -1200.0
                 :sx        0
                 :sy        0})))

(def screen
  {:minx   -2200
   :miny   -1600
   :maxx   350
   :maxy   350
   :width  (+ 350 2200)
   :height (+ 350 1600)})

(comment "Whats up with this? maxes are always max float and mins are min body")
(defn reducer [agg {:keys [x y]}]
  (-> agg
      (update :minx #(min % x))
      (update :miny #(min % y))
      (update :maxx #(max % x))
      (update :maxy #(max % y))))

(defn compute-boundaries [bodies]
  (reduce reducer
          {:minx 0
           :miny 0
           :maxx 0
           :maxy 0}
          bodies))

(defn compute-sector-matrix [bodies boundaries]
  (reduce m/add {:boundaries       boundaries
                 :sector-precision sector-precision}
          bodies))

(defn compute-quad [sector-matrix]
  )

#_(defn step []
    (let [bodies (:bodies @state)
          boundaries (compute-boundaries bodies)
          sector-matrix (compute-sector-matrix bodies boundaries)]
      sector-matrix))

(defn inquad? [quad body]
  (b/inbound? (:boundaries quad) body))

(defn add-to-quad [quad body]
  (update quad :bodies (fn [coll] (conj coll body))))

(defn quad-reducer [agg body]
  (cond
    (inquad? (agg 0) body) (update agg 0 #(add-to-quad % body))
    (inquad? (agg 1) body) (update agg 1 #(add-to-quad % body))
    (inquad? (agg 2) body) (update agg 2 #(add-to-quad % body))
    (inquad? (agg 3) body) (update agg 3 #(add-to-quad % body))))

(defn ->quadtree [boundary bodies]
  (let [split (b/quad-split boundary)
        quads (reduce quad-reducer
                      {0 {:boundaries (split 0)}
                       1 {:boundaries (split 1)}
                       2 {:boundaries (split 2)}
                       3 {:boundaries (split 3)}}
                      bodies)]
    (reduce (fn [quads key]
              (let [{:keys [boundaries bodies]} (quads key)]
                (if (<= (count bodies) 1)
                  quads
                  (assoc-in quads [key :quads] (->quadtree boundaries bodies)))))
            quads
            (range 4))))
