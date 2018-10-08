package org.vea.tutorial.services;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.vea.tutorial.controllers.dto.User;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UserServiceImplTest {

    @Test
    public void getUsers() {
        UserServiceImpl service = new UserServiceImpl();
        List<User> users = service.getUsers();

        assertThat(users, Matchers.<Collection<User>>allOf(
                instanceOf(List.class),
                hasSize(3),
                hasItem(allOf(
                        instanceOf(User.class),
                        hasProperty("firstName", equalTo("Eugene")),
                        hasProperty("lastName", equalTo("Volkoedov")),
                        hasProperty("nationality", equalTo("russian")),
                        hasProperty("admin", equalTo(true))
                )))
        );

    }
}