(ns barnes-hut.button-panel)

(def button-panel
  {:fx/type  :v-box
   :children [{:fx/type  :h-box
               :children [{:fx/type :label
                           :text    "parallellism"}
                          {:fx/type :combo-box
                           :items   [1 2 3 4]
                           :value   2}]}
              {:fx/type  :h-box
               :children [{:fx/type :label
                           :text    "total bodies"}
                          {:fx/type       :spinner
                           :value-factory {:fx/type           :integer-spinner-value-factory
                                           :min               32
                                           :max               1000000
                                           :value             25000
                                           :amount-to-step-by 1000}}]}
              {:fx/type  :h-box
               :children [{:fx/type :button
                           :text    "step"}
                          {:fx/type :button
                           :text    "start/stop"}]}
              {:fx/type  :h-box
               :children [{:fx/type :check-box
                           :text    "Show quad"}
                          {:fx/type :button
                           :text    "reset"}]}
              {:fx/type :text-area
               :text    "--- Statistics: ---"}]})
