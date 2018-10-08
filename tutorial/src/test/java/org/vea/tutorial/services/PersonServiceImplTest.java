package org.vea.tutorial.services;

import org.junit.Before;
import org.junit.Test;
import org.vea.tutorial.controllers.dto.Person;

import java.util.Map;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PersonServiceImplTest {

    private PersonService personService;
    private Person testPerson;
    private Person fakePerson;

    @Before
    public void setUp() {
        personService = new PersonServiceImpl();

        testPerson = Person.builder()
                .name("Ivan")
                .father("Ivan")
                .build();

        fakePerson = Person.builder()
                .name("Bo")
                .father("Jack")
                .build();
    }

    @Test
    public void getPersonMap() {
        Map<String, Person> result = personService.getPersonMap();

        assertThat(result, allOf(
                instanceOf(Map.class),
                hasEntry(testPerson.getName(), testPerson),
                not(hasEntry(fakePerson.getName(), fakePerson))
                )
        );

    }

    @Test
    public void getPersonArray() {
        Person[] result = personService.getPersonArray();

        assertThat(result, allOf(
                arrayWithSize(2),
                hasItemInArray(testPerson),
                not(hasItemInArray(fakePerson))
                )
        );

    }

    @Test
    public void getPerson() {
        Person result = personService.getPerson();

        assertThat(result, allOf(
                instanceOf(Person.class),
                equalTo(testPerson)
                )
        );
    }

}