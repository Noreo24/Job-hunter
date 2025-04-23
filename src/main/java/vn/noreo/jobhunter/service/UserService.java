package vn.noreo.jobhunter.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.noreo.jobhunter.domain.User;
import vn.noreo.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User handleCreateUser(User newUser) {
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
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
