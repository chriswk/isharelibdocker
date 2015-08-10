package com.chriswk.isharelib.tmdbimport;

import com.chriswk.isharelib.domain.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PersonImporter extends Importer<Person> {
    private static final Logger LOG = LogManager.getLogger();

    public PersonImporter() {
        super(Person.class);
    }

    @Override
    Person apiCall(Integer id) {
        return new Person(api.getPeople().getPersonInfo(id));
    }
}
