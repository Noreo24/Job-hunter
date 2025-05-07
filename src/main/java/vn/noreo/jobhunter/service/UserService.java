package vn.noreo.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.noreo.jobhunter.domain.User;
import vn.noreo.jobhunter.domain.dto.Meta;
import vn.noreo.jobhunter.domain.dto.ResultPaginationDTO;
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

    public User handleFetchUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public ResultPaginationDTO handleFetchAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setCurrentPage(userPage.getNumber() + 1);
        meta.setPageSize(userPage.getSize());
        meta.setTotalPages(userPage.getTotalPages());
        meta.setTotalItems(userPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setDataResult(userPage.getContent());

        return resultPaginationDTO;
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
