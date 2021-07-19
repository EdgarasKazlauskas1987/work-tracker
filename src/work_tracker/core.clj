(ns work-tracker.core
  (:require [seesaw.core :as seesaw]))


(defn -main [& args]
  (defn display
    [content]
    (let [window (seesaw/frame :title "Example")]
      (-> window
          (seesaw/config! :content content)
          (seesaw/pack!)
          (seesaw/show!))))
  (def button
    (seesaw/button
      :text "Click Me"
      :listen [:action (fn [event](seesaw/alert "Click!" ))]))
  (display button))


