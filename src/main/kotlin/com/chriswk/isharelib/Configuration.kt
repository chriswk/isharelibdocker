package com.chriswk.isharelib

import com.natpryce.konfig.*

import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.Key
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType
import java.io.File

private val localProperties = ConfigurationMap(
    mapOf(
        "application.profile" to "LOCAL",
        "port" to "8080",
        "tmdb.api.key" to "nada",
        "cache.folder" to "${System.getProperty("user.home")}/.isharelib"
    )
)

private val devProperties = ConfigurationMap(
    mapOf(
        "application.profile" to "DEV",
        "port" to "8080",
        "tmdb.api.key" to "devkey",
        "cache.folder" to "${System.getProperty("user.home")}/.isharelib"
    )
)

private val prodProperties = ConfigurationMap(
    mapOf(
        "application.profile" to "PROD",
        "port" to "8080",
        "tmdb.api.key" to "prodkey",
        "cache.folder" to "${System.getProperty("user.home")}/.isharelib"
    )
)

data class Configuration(
    val httpPort: Int = config()[Key("port", intType)],
    val profile: Profile = config()[Key("application.profile", stringType)].let { Profile.valueOf(it) },
    val tmdbApiKey: String = config()[Key("tmdb.api.key", stringType)],
    val cacheFolder: File = File(config()[Key("cache.folder", stringType)])
)

enum class Profile {
    LOCAL, DEV, PROD
}

fun getEnvOrProp(propName: String): String? {
    return System.getenv(propName) ?: System.getProperty(propName)
}

private fun config() = when (getEnvOrProp("PROFILE")) {
    "dev" -> ConfigurationProperties.systemProperties() overriding EnvironmentVariables overriding devProperties
    "prod" -> ConfigurationProperties.systemProperties() overriding EnvironmentVariables overriding prodProperties
    else -> ConfigurationProperties.systemProperties() overriding EnvironmentVariables overriding localProperties
}