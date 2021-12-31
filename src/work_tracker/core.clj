(ns work-tracker.core
  (:gen-class)
  (:import (java.awt Color))
  (:require [seesaw.core :as seesaw]
            [work-tracker.utils :as utils]
            [puppetlabs.trapperkeeper.services.scheduler.scheduler-core :as trapper]))

(def job-id (atom nil))
(def active-window (atom nil))

(defn setup [window panel]
  (as-> window v
        (seesaw/config! v :content panel)
        (seesaw/show! v)
        (reset! active-window v)))

(defn -main []
  (utils/create-new-file)
  (utils/create-config-file)
  (let [scheduler (trapper/create-scheduler 1)
        window (seesaw/frame :title (str "Work Tracker " (utils/current-time))
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
        save-changes-btn (seesaw/button :text "Save changes" :size [170 :by 30] :listen [:action (fn [event] (do
                                                                                                               (utils/save-time-config (seesaw/text time-txt))
                                                                                                               (trapper/stop-job @job-id scheduler)
                                                                                                               (->> (trapper/interval scheduler (utils/min-to-mls (Integer/parseInt (seesaw/text time-txt)))
                                                                                                                                      (fn [] (seesaw/show! @active-window)) "work-tracker")
                                                                                                                    (reset! job-id))))])
        top-panel (seesaw/horizontal-panel :items [msg-txt notification-lbl time-txt min-lbl])
        bottom-panel (seesaw/horizontal-panel :items [save-btn open-btn close-day-btn save-changes-btn])
        overall-panel (seesaw/vertical-panel :items [top-panel bottom-panel])]
    (->> (trapper/interval scheduler 10000 (fn [] (setup window overall-panel)) "work-tracker")
         (reset! job-id))))