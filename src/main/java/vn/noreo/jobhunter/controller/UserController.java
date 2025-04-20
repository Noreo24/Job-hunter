package vn.noreo.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.noreo.jobhunter.domain.User;
import vn.noreo.jobhunter.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/create")
    public User createNewUser(@RequestBody User newUser) {
        User user = this.userService.handleCreateUser(newUser);
        return user;
    }
}
