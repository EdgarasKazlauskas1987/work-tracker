(defproject work-tracker "0.1.2-SNAPSHOT"
  :description "Clojure application for tracking work tasks"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [seesaw "1.5.0"]
                 [org.clojure/data.csv "1.0.0"]
                 [puppetlabs/trapperkeeper-scheduler "1.1.3"]]
  :main         work-tracker.core
  :repl-options {:init-ns work-tracker.core})