(ns work-tracker.utils-test
  (:require [clojure.test :refer :all]
            [work-tracker.utils :as utils]))

(deftest min-to-ms-test
  (testing "Conversion from minutes to milliseconds"
    (is (= 60000 (utils/min-to-ms 1)))
    (is (= 300000 (utils/min-to-ms 5)))))