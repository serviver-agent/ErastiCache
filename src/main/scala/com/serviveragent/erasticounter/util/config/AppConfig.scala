package com.serviveragent.erasticounter.util.config

import com.typesafe.config.ConfigFactory

trait AppConfig {
  val appConfig = AppConfig.config
}

object AppConfig {
  val config = ConfigFactory.load()
}
