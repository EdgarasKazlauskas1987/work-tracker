(ns work-tracker.core
  (:gen-class)
  (:import (java.time Instant Duration))
  (:require [seesaw.core :as seesaw]
            [work-tracker.utils :as utils]
            [chime.core :as chm]))


(defn setup [window panel]
  (-> window
      (seesaw/config! :content panel)
      (seesaw/show!)))

(defn -main []
  (utils/create-new-file)
  (let [window (seesaw/frame :title (str "Work Tracker " (utils/current-time))
                             :width 600 :height 80 :resizable? false :on-close :hide)
        text-field (seesaw/text :text (second (utils/read-last-work)) :editable? true :multi-line? false)
        save-btn (seesaw/button :text "Save" :preferred-size [130 :by 30] :listen [:action (fn [event] (utils/save-work
                                                                                                         (utils/current-time)
                                                                                                         (seesaw/text text-field)))])
        open-btn (seesaw/button :text "Open" :preferred-size [130 :by 30] :listen [:action (fn [event] (if (utils/supported?)
                                                                                                         (utils/open-file)
                                                                                                         (seesaw/alert "Not supported on this OS"
                                                                                                                       :title "Error"
                                                                                                                       :type :error)))])
        close-day-btn (seesaw/button :text "Close the Day" :preferred-size [180 :by 30] :listen [:action (fn [event] (System/exit 1))])
        top-panel (seesaw/horizontal-panel :items [text-field])
        bottom-panel (seesaw/horizontal-panel :items [save-btn open-btn close-day-btn])
        overall-panel (seesaw/vertical-panel :items [top-panel bottom-panel])]
    (setup window overall-panel)
    (chm/chime-at (chm/periodic-seq (Instant/now) (Duration/ofMinutes 30))
                  (fn [_]
                    (setup window overall-panel)))))