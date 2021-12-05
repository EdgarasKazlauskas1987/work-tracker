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
                             :width 900 :height 56 :resizable? false :on-close :hide)
        text-field (seesaw/text :text (first (utils/read-last-work)) :editable? true :multi-line? false)
        save-btn (seesaw/button :text "Save" :preferred-size [100 :by 30] :listen [:action (fn [event] (utils/save-work
                                                                                                         (seesaw/text text-field)
                                                                                                         (utils/current-time)))])
        open-btn (seesaw/button :text "Open" :preferred-size [100 :by 30] :listen [:action (fn [event] (if (utils/supported?)
                                                                                                         (utils/open-file)
                                                                                                         (seesaw/alert "Not supported on this OS"
                                                                                                                       :title "Error"
                                                                                                                       :type :error)))])
        close-day (seesaw/button :text "Close the Day" :preferred-size [150 :by 30] :listen [:action (fn [event] (System/exit 1))])
        panel (seesaw/horizontal-panel :items [text-field save-btn open-btn close-day])]
    (setup window panel)
    (chm/chime-at (chm/periodic-seq (Instant/now) (Duration/ofSeconds 20))
                  (fn [_]
                    (setup window panel)))))