package com.chriswk.isharelib.domain;

import info.movito.themoviedbapi.model.MovieDb;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movie extends Identifiable {
    private static final Logger LOG = LogManager.getLogger();
    private static final DateTimeFormatter bdayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String imdbId;
    private String title;
    private long budget;
    private String overview;
    private LocalDateTime releaseDate;

    public Movie(MovieDb tmdbMovie) {
        super(tmdbMovie.getId());
        this.imdbId = tmdbMovie.getImdbID();
        this.title = tmdbMovie.getTitle();
        this.budget = tmdbMovie.getBudget();
        this.overview = tmdbMovie.getOverview();
        this.releaseDate = getDate(tmdbMovie.getReleaseDate());
    }

    public Movie(int id, String imdbId, String title, long budget, String overview, LocalDateTime releaseDate) {
        super(id);
        this.imdbId = imdbId;
        this.title = title;
        this.budget = budget;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public Movie() {}

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("imdbId", imdbId)
                .append("title", title)
                .append("budget", budget)
                .append("releaseDate", releaseDate)
                .toString();
    }
}
