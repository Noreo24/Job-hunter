package vn.noreo.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.noreo.jobhunter.domain.User;
import vn.noreo.jobhunter.domain.dto.ResultPaginationDTO;
import vn.noreo.jobhunter.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User newUser) {
        User user = this.userService.handleCreateUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
        // return ResponseEntity.status(HttpStatus.OK).body("Deleted user with id: " +
        // id);
        // return ResponseEntity.ok("Deleted user with id: " + id);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleFetchUserById(id));
        // return ResponseEntity.ok(this.userService.handleFetchUserById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUser(
            @RequestParam("current") Optional<String> currentPageOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String currentPage = currentPageOptional.isPresent() ? currentPageOptional.get() : "";
        String pageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        Pageable pageable = PageRequest.of(Integer.parseInt(currentPage) - 1, Integer.parseInt(pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleFetchAllUsers(pageable));
        // return ResponseEntity.ok(this.userService.handleFetchAllUser());
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        User user = this.userService.handleUpdateUser(updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(user);
        // return ResponseEntity.ok(user);
    }
}
