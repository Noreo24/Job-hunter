package vn.noreo.jobhunter.service;

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
}
