package com.chriswk.isharelib.domain;

import info.movito.themoviedbapi.model.tv.TvSeries;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

public class Series extends Identifiable {

    private int numberOfEpisodes;
    private int numberOfSeasons;
    private LocalDateTime firstAirDate;
    private String name;
    private String originalName;

    public Series(TvSeries tvSeries) {
        super(tvSeries.getId());
        this.name = tvSeries.getName();
        this.numberOfEpisodes = tvSeries.getNumberOfEpisodes();
        this.numberOfSeasons = tvSeries.getNumberOfSeasons();
        this.firstAirDate = getDate(tvSeries.getFirstAirDate());
        this.originalName = tvSeries.getOriginalName();
    }
    public Series() {}

    public Series(int id, int numberOfEpisodes, int numberOfSeasons, LocalDateTime firstAirDate, String name, String originalName) {
        super(id);
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
        this.firstAirDate = firstAirDate;
        this.name = name;
        this.originalName = originalName;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public LocalDateTime getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(LocalDateTime firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .append("numberOfEpisodes", numberOfEpisodes)
            .append("numberOfSeasons", numberOfSeasons)
            .append("firstAirDate", firstAirDate)
            .append("name", name)
            .append("originalName", originalName)
            .toString();
    }
}
