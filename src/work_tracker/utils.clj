(ns work-tracker.utils
  (:import (java.text SimpleDateFormat)
           (java.util Date)
           (java.io File)
           (java.awt Desktop)
           (java.util.concurrent TimeUnit))
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn min-to-ms [min]
  (.convert (TimeUnit/MILLISECONDS) min TimeUnit/MINUTES))

(defn current-time []
  (.format (SimpleDateFormat. "yyyy-MM-dd HH:mm") (new Date)))

(defn current-year []
  (.format (SimpleDateFormat. "yyyy") (new Date)))

(defn current-month []
  (.format (SimpleDateFormat. "MMMM") (new Date)))

(defn current-day []
  (.format (SimpleDateFormat. "dd") (new Date)))

(def base-path (System/getenv "WORK_FOLDER"))

(defn generate-paths [base-path]
  {:config-path (str base-path "\\config.txt")
   :year-path   (str base-path "\\" (current-year))
   :month-path  (str base-path "\\" (current-year) "\\" (current-month))
   :full-path   (str base-path "\\" (current-year) "\\" (current-month) "\\" (current-day) ".txt")})

(defn create-work-file []
  (let [{:keys [year-path month-path full-path]} (generate-paths base-path)
        work-file (new File full-path)]
    (when-not (.exists (io/file full-path))
      (.mkdir (File. year-path))
      (.mkdir (File. month-path))
      (.createNewFile work-file))))

(defn save-time-config [^String min]
  (let [config-path (:config-path (generate-paths base-path))]
    (with-open [writer (io/writer config-path)]
      (.write writer min))))

(defn create-config-file [min]
  (let [config-path (:config-path (generate-paths base-path))
        config-file (new File config-path)]
    (when-not (.exists (io/file config-path))
      (.createNewFile config-file)
      (save-time-config min))))

(defn read-time-config []
  (slurp (:config-path (generate-paths base-path))))

(defn supported? []
  (Desktop/isDesktopSupported))

(defn open-file []
  (let [{full-path :full-path} (generate-paths base-path)
        f (new File full-path)
        d (Desktop/getDesktop)]
    (.edit d f)))

(defn read-last-work []
  (let [{full-path :full-path} (generate-paths base-path)]
    (with-open [reader (io/reader full-path)]
      (last
        (doall
          (csv/read-csv reader))))))

(defn save-work [date task]
  (let [{full-path :full-path} (generate-paths base-path)]
    (create-work-file)
    (with-open [writer (io/writer full-path :append true)]
      (csv/write-csv writer [[date task]] :newline :cr+lf))))