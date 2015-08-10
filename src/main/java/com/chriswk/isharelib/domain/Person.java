package com.chriswk.isharelib.domain;

import info.movito.themoviedbapi.model.people.PersonPeople;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class Person extends Identifiable {
    private static final Logger LOG = LogManager.getLogger();
    private static final DateTimeFormatter bdayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String name;
    private List<String> aliases;
    private String biography;
    private LocalDateTime birthDate;
    private String birthPlace;
    private LocalDateTime deathDate;
    private String imdbId;

    public Person(PersonPeople tmdbData) {
        super(tmdbData.getId());
        this.name = tmdbData.getName();
        this.aliases = tmdbData.getAka();
        this.biography = tmdbData.getBiography();
        this.birthDate = getDate(tmdbData.getBirthday());
        this.birthPlace = tmdbData.getBirthplace();
        this.deathDate = getDate(tmdbData.getDeathday());
        this.imdbId = tmdbData.getImdbId();
    }

    public Person() {
    }

    public Person(int id, String name, List<String> aliases, String biography, LocalDateTime birthDate, String birthPlace, LocalDateTime deathDate, String imdbId) {
        super(id);
        this.name = name;
        this.aliases = aliases;
        this.biography = biography;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.deathDate = deathDate;
        this.imdbId = imdbId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LocalDateTime getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDateTime deathDate) {
        this.deathDate = deathDate;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    private LocalDateTime getDate(String dateString) {
        try {
            return LocalDate.parse(dateString, bdayFormatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            LOG.warn("Failed to parse dateString: {}", dateString);
        }
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("name", name)
                .append("birthDate", birthDate)
                .append("birthPlace", birthPlace)
                .append("deathDate", deathDate)
                .append("imdbId", imdbId)
                .toString();
    }
}
