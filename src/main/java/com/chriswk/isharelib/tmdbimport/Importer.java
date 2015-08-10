package com.chriswk.isharelib.tmdbimport;

import com.chriswk.isharelib.domain.Identifiable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.movito.themoviedbapi.TmdbApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public abstract class Importer<T extends Identifiable> {
    private static final Logger LOG = LogManager.getLogger();
    private final File cacheFolder;
    private final File localFolder;
    private final String importObjectName;
    private final Class<T> classObject;
    ObjectMapper mapper;
    TmdbApi api;

    public Importer(Class<T> type) {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.api = getProperty("TMDB_API_KEY")
                .map(TmdbApi::new)
                .orElseThrow(() -> new IllegalArgumentException("Missing api key"));
        this.classObject = type;
        this.importObjectName = classObject.getSimpleName();
        this.cacheFolder = getProperty("CACHE_FOLDER").map(File::new).orElse(new File(System.getProperty("user.home"), ".isharelibdata"));
        this.localFolder = new File(this.cacheFolder, classObject.getSimpleName());
        this.localFolder.mkdirs();
    }

    public void cacheObject(T t) {
        String path = t.getId() +".json";
        try {
            mapper.writeValue(new File(localFolder, path), t);
        } catch (IOException e) {
            LOG.error("Could not cache " +importObjectName +" [ " +t +"]", e);
        }
    }

    public T getCachedObject(Integer id) {
        String path = String.format("%d.json", id);
        LOG.info("Checking [{}] for cache", path);
        try {
            return mapper.readValue(new File(localFolder, path), classObject);
        } catch (IOException e) {
            LOG.error("Could not get " + importObjectName + " with id: " + id, e);
        }
        return null;
    }

    public T fetchObject(int id) {
        return Optional.ofNullable(getCachedObject(id)).orElseGet(() -> {
            T f = apiCall(id);
            cacheObject(f);
            return f;
        });
    }

    abstract T apiCall(Integer id);

    public Optional<String> getProperty(String propName) {
        Optional<String> property = Optional.ofNullable(System.getProperty(propName));
        return property.isPresent() ? property : Optional.ofNullable(System.getenv(propName));
    }
}
