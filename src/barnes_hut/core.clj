(ns barnes-hut.core
  (:require [cljfx.api :as fx]
            [barnes-hut.button-panel :refer [button-panel]]
            [barnes-hut.simulator :refer [init-2-galaxies]]
            [barnes-hut.view :refer [sim-view]]
            [barnes-hut.state :refer [state]])
  )

(reset! state {:bodies (init-2-galaxies)})

(defn root [{:keys [bodies]}]
  {:fx/type :stage
   :showing true
   :scene   {:fx/type :scene
             :root    {:fx/type :border-pane
                       :center  {:fx/type sim-view
                                 :width   800
                                 :height  600
                                 :bodies  bodies}
                       :right   button-panel}}})

(def renderer
  (fx/create-renderer
    :middleware (fx/wrap-map-desc assoc :fx/type root)))

(fx/mount-renderer state renderer)
