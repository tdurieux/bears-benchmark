package org.vea.tutorial.services;

import org.springframework.stereotype.Service;
import org.vea.tutorial.controllers.dto.Person;

import java.util.HashMap;
import java.util.Map;

@Service
public class PersonServiceImpl implements PersonService {
    private Person first;
    private Person second;

    public PersonServiceImpl() {
        first = Person.builder()
                .name("Eugene")
                .father("Anatoliy")
                .build();

        second = Person.builder()
                .name("Ivan")
                .father("Ivan")
                .build();
    }

    @Override
    public Map<String,Person> getPersonMap() {

        Map<String, Person> personsByName = new HashMap<>();

        personsByName.put(first.getName(), first);

        personsByName.put(second.getName(), second);

        return personsByName;


    }

    @Override
    public Person[] getPersonArray() {
        Person[] people = new Person[2];
        people[0] = first;
        people[1] = second;
        return people;
    }

    @Override
    public Person getPerson() {
        return second;
    }
}
