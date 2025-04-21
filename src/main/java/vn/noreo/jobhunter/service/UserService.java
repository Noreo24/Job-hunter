package vn.noreo.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.noreo.jobhunter.domain.User;
import vn.noreo.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User newUser) {
        return this.userRepository.save(newUser);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleFetchUserById(long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public List<User> handleFetchAllUser() {
        return this.userRepository.findAll();
    }

    public User handleUpdateUser(User updatedUser) {
        User currentUser = this.handleFetchUserById(updatedUser.getId());
        if (currentUser != null) {
            currentUser.setName(updatedUser.getName());
            currentUser.setEmail(updatedUser.getEmail());
            currentUser.setPassword(updatedUser.getPassword());

            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
        // return this.userRepository.save(updatedUser);
    }
}
