(ns work-tracker.utils
  (:import (java.text SimpleDateFormat)
           (java.util Date)
           (java.io File))
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def path "C:\\Users\\Edgaras\\Desktop\\WorkFolder")

(defn current-time []
  (.format (SimpleDateFormat. "yyyy-MM-dd HH:mm") (new Date)))

(defn current-date []
  (.format (SimpleDateFormat. "yyyy-MM-dd") (new Date)))

(defn current-year []
  (.format (SimpleDateFormat. "yyyy") (new Date)))

(defn current-month []
  (.format (SimpleDateFormat. "MMMM") (new Date)))

(defn current-day []
  (.format (SimpleDateFormat. "dd") (new Date)))

(defn create-new-file []
  (let [year-path (str path "\\" (current-year))
        month-path (str path "\\" (current-year) "\\" (current-month))
        full-path (str path "\\" (current-year) "\\" (current-month) "\\" (current-day) ".txt")]
   (when-not (.exists (io/file full-path))
     (do (.mkdir (File. year-path))
         (.mkdir (File. month-path))
         (spit full-path "testing")))))

(defn read-work []
  (with-open [reader (io/reader path)]
    (doall
      (csv/read-csv reader))))

(defn read-last-work []
  (let [full-path (str path "\\" (current-year) "\\" (current-month) "\\" (current-day) ".txt")]
   (with-open [reader (io/reader full-path)]
    (last
      (doall
       (csv/read-csv reader))))))

(defn save-work [task date]
  (let [year (current-year)
        month (current-month)
        day (current-day)
        full-path (str path "\\" year "\\" month "\\" day ".txt")]
    (create-new-file)
   (with-open [writer (io/writer full-path :append true)]
    (csv/write-csv writer [[task date]] :newline :cr+lf ))))
