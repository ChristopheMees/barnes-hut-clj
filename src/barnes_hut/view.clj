(ns barnes-hut.view
  (:require [barnes-hut.simulator :refer [screen]])
  (:import [javafx.scene.canvas Canvas]
           [javafx.scene.paint Color]
           [javafx.embed.swing SwingFXUtils]
           [java.awt.image BufferedImage]))

(def max-res 3000)
(def width 800)
(def height 600)

(def width-ratio (/ width (:width screen)))
(def height-ratio (/ height (:height screen)))

(defn calcx [x]
  (Math/round
    (float
      (* (- x (:minx screen)) width-ratio))))

(defn calcy [y]
  (Math/round
    (float
      (* (- y (:miny screen)) height-ratio))))

(defn ninc [x]
  (if x (inc x) 1))

(defn reducer [agg {:keys [x y]}]
  (update agg {:x (calcx x) :y (calcy y)} ninc))

(defn to-argb [x]
  (.getRGB (new java.awt.Color x x x 255)))

(defn intensity [x]
  (if (> x 0)
    (min 255, (+ 70 (* x 50)))
    0))

(defn canvas-render [bodies]
  (fn [^Canvas canvas]
    (let [width 800
          height 600
          img (new BufferedImage width height BufferedImage/TYPE_INT_ARGB)
          img-graphics (.getGraphics img)
          pixels (reduce reducer {} bodies)
          graphicsCtx (.getGraphicsContext2D canvas)]
      (doseq [x (range 0 width)
              y (range 0 height)]
        (let [weight (or (pixels {:x x :y y}) 0)]
          (.setRGB img x y (to-argb (intensity weight)))))
      (.drawImage graphicsCtx (SwingFXUtils/toFXImage img nil) 0 0))))

(defn sim-view [{:keys [width height bodies]}]
  {:fx/type :canvas
   :width   width
   :height  height
   :draw    (canvas-render bodies)})
