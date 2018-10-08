package org.vea.tutorial.services;

import org.vea.tutorial.controllers.dto.Person;

import java.util.Map;


public interface PersonService {
    Map<String,Person> getPersonMap();

    Person[] getPersonArray();

    Person getPerson();
}
