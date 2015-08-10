package com.chriswk.isharelib.tmdbimport;

import com.chriswk.isharelib.domain.Movie;
import org.springframework.stereotype.Service;

@Service
public class MovieImporter extends Importer<Movie> {
    public MovieImporter() {
        super(Movie.class);
    }

    @Override
    Movie apiCall(Integer id) {
        return new Movie(api.getMovies().getMovie(id, "en"));
    }


}
