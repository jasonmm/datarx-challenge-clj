(defproject datarx-challenge "0.1.0-SNAPSHOT"
  :description "DataRx's programming problem to find a given word in a word search grid"
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :plugins [[lein-cljfmt "0.5.3"]]
  :main ^:skip-aot datarx-challenge.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
