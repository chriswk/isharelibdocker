package com.chriswk.isharelib.tmdbimport;

import com.chriswk.isharelib.domain.Series;

public class SeriesImporter extends Importer<Series> {
    public SeriesImporter() {
        super(Series.class);
    }

    @Override
    Series apiCall(Integer id) {
        return new Series(api.getTvSeries().getSeries(id, "en"));
    }
}
