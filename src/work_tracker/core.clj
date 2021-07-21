(ns work-tracker.core
  (:require [seesaw.core :as seesaw]))

;; ToDo make function to save work record to DB
(defn save-work [task]
  (println task))

(defn -main [& args]
  (let [window (seesaw/frame :title "Work Tracker" :width 300 :height 300 :on-close :hide)
        text-field (seesaw/text :text "Add work" :editable? true :columns 20)
        button (seesaw/button :text "Click Me" :listen [:action (fn [event](save-work (seesaw/text text-field)))])
        panel (seesaw/horizontal-panel :items [text-field button])]
    (seesaw/config! window :content panel)
    (seesaw/show! window)))