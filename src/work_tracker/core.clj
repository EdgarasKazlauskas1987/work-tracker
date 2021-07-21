(ns work-tracker.core
  (:require [seesaw.core :as seesaw]
            [work-tracker.utils :as utils]))

(defn -main [& args]
  (utils/create-new-file)
  (let [window (seesaw/frame :title (str "Work Tracker " (utils/current-time))
                             :width 900 :height 56 :resizable? false :on-close :hide)
        text-field (seesaw/text :text (first (utils/read-last-work)) :editable? true :multi-line? false)
        button (seesaw/button :text "Click Me" :preferred-size [100 :by 30] :listen [:action (fn [event] (utils/save-work
                                                                                                           (seesaw/text text-field)
                                                                                                           (utils/current-time)))])
        panel (seesaw/horizontal-panel :items [text-field button])]
    (-> window
        (seesaw/config! :content panel)
        (seesaw/show!))))