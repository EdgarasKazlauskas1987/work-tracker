(ns work-tracker.core
  (:gen-class)
  (:import (java.time Instant Duration)
           (java.awt Color))
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
                             :width 700 :height 95 :resizable? false :on-close :hide)
        msg-txt (seesaw/text :text (second (utils/read-last-work)) :editable? true :multi-line? false :size [530 :by 35])
        notification-lbl (seesaw/label :text "Remind in" :size [60 :by 35])
        time-txt (seesaw/text :text "30" :editable? true :multi-line? false :size [50 :by 35])
        min-lbl (seesaw/label :text "min." :size [60 :by 35])
        save-btn (seesaw/button :text "Save" :size [140 :by 30] :background (Color. 101,226,159) :listen [:action (fn [event] (utils/save-work
                                                                                                         (utils/current-time)
                                                                                                         (seesaw/text msg-txt)))])
        open-btn (seesaw/button :text "Open" :size [140 :by 30] :listen [:action (fn [event] (if (utils/supported?)
                                                                                                         (utils/open-file)
                                                                                                         (seesaw/alert "Not supported on this OS"
                                                                                                                       :title "Error"
                                                                                                                       :type :error)))])
        close-day-btn (seesaw/button :text "Close the Day" :size [250 :by 30] :listen [:action (fn [event] (System/exit 1))])
        save-changes-btn (seesaw/button :text "Save changes" :size [170 :by 30])
        top-panel (seesaw/horizontal-panel :items [msg-txt notification-lbl time-txt min-lbl])
        bottom-panel (seesaw/horizontal-panel :items [save-btn open-btn close-day-btn save-changes-btn])
        overall-panel (seesaw/vertical-panel :items [top-panel bottom-panel])]
    (setup window overall-panel)
    (chm/chime-at (chm/periodic-seq (Instant/now) (Duration/ofMinutes 30))
                  (fn [_]
                    (setup window overall-panel)))))