package com.chriswk.isharelib;

import com.chriswk.isharelib.tmdbimport.MovieImporter;
import com.chriswk.isharelib.tmdbimport.PersonImporter;
import com.chriswk.isharelib.tmdbimport.SeriesImporter;

import static java.util.Arrays.asList;

public class IsharelibApp {

    public static void main(String... args) {
        /*SpringApplication.run(IsharelibConfiguration.class);*/
        PersonImporter personImporter = new PersonImporter();
        asList(55751, 56825, 1223786, 1100).stream().map(personImporter::fetchObject
        ).forEach(System.out::println);
        MovieImporter movieImporter = new MovieImporter();
        asList(87101, 167948).stream().map(movieImporter::fetchObject
        ).forEach(System.out::println);
        SeriesImporter seriesImporter = new SeriesImporter();
        asList(1399, 456).stream().map(seriesImporter::fetchObject).forEach(System.out::println);
    }
}
