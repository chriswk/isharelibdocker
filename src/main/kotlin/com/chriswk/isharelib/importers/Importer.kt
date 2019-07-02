package com.chriswk.isharelib.importers

import com.chriswk.isharelib.Configuration
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import info.movito.themoviedbapi.TmdbApi
import info.movito.themoviedbapi.model.MovieDb
import info.movito.themoviedbapi.model.people.PersonPeople
import info.movito.themoviedbapi.model.tv.TvSeries
import mu.KotlinLogging
import java.io.File
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val logger = KotlinLogging.logger {}

abstract class Importer<T : Identifiable>(val type: Class<T>) {
    companion object {
        private val config = Configuration()
        val objectMapper = jacksonObjectMapper()
    }

    private val localFolder: File = File(config.cacheFolder, type.simpleName)

    init {
        localFolder.mkdirs()
    }

    val api by lazy { TmdbApi(config.tmdbApiKey) }

    fun cacheObject(t: T) {
        val path = "${t.id}.json"
        runCatching {
            objectMapper.writeValue(File(localFolder, path), t)
        }.fold(
            onSuccess = { logger.info("Cached ${t.id}") },
            onFailure = { failure -> logger.warn("Could not cache ${t.id}", failure) })
    }

    fun getCachedObject(id: Long): T? {
        val path = "$id.json"
        logger.info { "Checking $path for cache" }
        return runCatching {
            objectMapper.readValue(File(localFolder, path), type)
        }.fold(
            onSuccess = { s -> logger.info { s }; s },
            onFailure = { t -> logger.warn("Failed to fetch cached object $id", t); return null })
    }

    fun fetchObject(id: Long): T? {
        val obj = getCachedObject(id) ?: apiCall(id)
        if (obj != null) {
            cacheObject(obj)
            logger.info("Cached to $localFolder/$id.json")
        }
        return obj
    }

    abstract fun apiCall(id: Long): T?
}

class MovieImporter : Importer<Movie>(Movie::class.java) {
    override fun apiCall(id: Long): Movie? {
        val movie = Movie.fromApiCall(api.movies.getMovie(id.toInt(), "en"))
        logger.info { movie }
        return movie
    }
}

class PersonImporter : Importer<Person>(Person::class.java) {
    override fun apiCall(id: Long): Person? {
        return Person.fromApiCall(api.people.getPersonInfo(id.toInt()))
    }
}

class SeriesImporter : Importer<Series>(Series::class.java) {
    override fun apiCall(id: Long): Series? {
        return Series.fromApiCall(api.tvSeries.getSeries(id.toInt(), "en"))
    }
}

data class Person(
    override val id: Long,
    val name: String,
    val aliases: List<String> = emptyList(),
    val biography: String,
    val birthDate: LocalDate?,
    val birthPlace: String,
    val deathDate: LocalDate? = null,
    val imdbId: String
) : Identifiable(id) {
    companion object {
        fun fromApiCall(tmdbPerson: PersonPeople): Person {
            return Person(
                id = tmdbPerson.id.toLong(),
                name = tmdbPerson.name,
                imdbId = tmdbPerson.imdbId,
                aliases = tmdbPerson.aka,
                biography = tmdbPerson.biography,
                birthDate = tmdbPerson.birthday.getDate(),
                birthPlace = tmdbPerson.birthplace,
                deathDate = tmdbPerson.deathday.getDate()
            )
        }
    }
}

data class Movie(
    override val id: Long,
    val title: String,
    val imdbId: String,
    val budget: Long,
    val overview: String,
    val releaseDate: String
) : Identifiable(id) {
    companion object {
        val objectMapper = jacksonObjectMapper()
        fun fromApiCall(tmdbMovie: MovieDb): Movie {
            return Movie(
                id = tmdbMovie.id.toLong(),
                title = tmdbMovie.title,
                imdbId = tmdbMovie.imdbID,
                budget = tmdbMovie.budget,
                overview = tmdbMovie.overview,
                releaseDate = tmdbMovie.releaseDate
            )
        }

        fun fromFile(fileName: String): Movie? {
            return fileName.toInputStream()?.let { objectMapper.readValue(it) }
        }
    }
}

data class Series(
    override val id: Long,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val firstAirDate: LocalDate? = null,
    val name: String,
    val originalName: String?
) : Identifiable(id) {
    companion object {
        fun fromApiCall(series: TvSeries): Series {
            return Series(
                id = series.id.toLong(),
                name = series.name,
                originalName = series.originalName,
                numberOfEpisodes = series.numberOfEpisodes,
                numberOfSeasons = series.numberOfSeasons,
                firstAirDate = series.firstAirDate.getDate()
            )
        }
    }
}

open class Identifiable(open val id: Long)

val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
fun String.getDate(): LocalDate? {
    return runCatching {
        LocalDate.parse(this, dateFormatter)
    }.fold(
        onSuccess = { success -> success },
        onFailure = { failure -> logger.warn("Failed to parse date $this", failure); return null }
    )
}

fun String.toInputStream(): InputStream? {
    return Importer::class.java.classLoader.getResourceAsStream(this)
}