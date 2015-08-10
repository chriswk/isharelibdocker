package com.chriswk.isharelib.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Identifiable {
    private static final Logger LOG = LogManager.getLogger();
    private static final DateTimeFormatter bdayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private int id;

    public int getId() {
        return id;
    }

    public Identifiable() {
    }

    public Identifiable(int id) {
        this.id = id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    LocalDateTime getDate(String dateString) {
        try {
            return LocalDate.parse(dateString, bdayFormatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            LOG.warn("Failed to parse dateString: {}", dateString);
        }
        return null;
    }

}
