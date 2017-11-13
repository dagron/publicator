(ns publicator.impl.test-data-source
  (:require
   [jdbc.core :as jdbc])
  (:import
   [com.mchange.v2.c3p0 ComboPooledDataSource]))

(def data-source
  (doto (ComboPooledDataSource.)
    (.setJdbcUrl "jdbc:postgresql://db/test")
    (.setUser "postgres")
    (.setPassword "password")))
