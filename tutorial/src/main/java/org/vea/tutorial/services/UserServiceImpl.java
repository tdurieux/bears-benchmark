package org.vea.tutorial.services;

import org.springframework.stereotype.Service;
import org.vea.tutorial.controllers.dto.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        User user = User.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .nationality("russian")
                .admin(false)
                .age(12)
                .build();
        users.add(user);

        user = User.builder()
                .firstName("Eugene")
                .lastName("Volkoedov")
                .nationality("russian")
                .admin(true)
                .age(35)
                .build();
        users.add(user);

        user = User.builder()
                .firstName("Jack")
                .lastName("Daniels")
                .nationality("american")
                .admin(false)
                .age(17)
                .build();
        users.add(user);

        return users;
    }
}
