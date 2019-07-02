package com.chriswk.isharelib

import com.chriswk.isharelib.importers.Movie
import com.chriswk.isharelib.importers.MovieImporter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class MovieImporterTest {
    @Test
    fun `should not call tmdb when file is already cached`() {
        val importer = MovieImporter()
        val movie = Movie.fromFile("167948.json")
        assertNotNull(movie)
        importer.cacheObject(movie!!)
        val cachedMovie = importer.fetchObject(167948)
        assertEquals(movie, cachedMovie)
    }
}