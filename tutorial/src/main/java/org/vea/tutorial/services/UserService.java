package org.vea.tutorial.services;

import org.vea.tutorial.controllers.dto.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
}
