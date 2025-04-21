package vn.noreo.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.noreo.jobhunter.domain.User;
import vn.noreo.jobhunter.service.UserService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User newUser) {
        User user = this.userService.handleCreateUser(newUser);
        return user;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return "Deleted user with id: " + id;
    }

    @GetMapping("/user/{id}")
    public User fetchUserById(@PathVariable("id") long id) {
        return this.userService.handleFetchUserById(id);
    }

    @GetMapping("/user")
    public List<User> fetchAllUser() {
        return this.userService.handleFetchAllUser();
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User updatedUser) {
        User user = this.userService.handleUpdateUser(updatedUser);
        return user;
    }
}
