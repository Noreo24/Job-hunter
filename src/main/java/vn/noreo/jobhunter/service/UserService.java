package vn.noreo.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.noreo.jobhunter.domain.User;
import vn.noreo.jobhunter.domain.response.ResCreateUserDTO;
import vn.noreo.jobhunter.domain.response.ResFetchUserDTO;
import vn.noreo.jobhunter.domain.response.ResUpdateUserDTO;
import vn.noreo.jobhunter.domain.response.ResultPaginationDTO;
import vn.noreo.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkUserExistsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO userDTO = new ResCreateUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setCreatedAt(user.getCreatedAt());
        return userDTO;
    }

    public ResFetchUserDTO convertToResFetchUserDTO(User user) {
        ResFetchUserDTO userDTO = new ResFetchUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO userDTO = new ResUpdateUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setAddress(user.getAddress());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
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
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setCurrentPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setTotalPages(userPage.getTotalPages());
        meta.setTotalItems(userPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);

        List<ResFetchUserDTO> userDTOs = userPage.getContent().stream()
                .map(item -> new ResFetchUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getAge(),
                        item.getGender(),
                        item.getAddress(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()))
                .collect(Collectors.toList());
        resultPaginationDTO.setDataResult(userDTOs);
        return resultPaginationDTO;
    }

    public User handleUpdateUser(User updatedUser) {
        User currentUser = this.handleFetchUserById(updatedUser.getId());
        if (currentUser != null) {
            currentUser.setName(updatedUser.getName());
            currentUser.setGender(updatedUser.getGender());
            currentUser.setAge(updatedUser.getAge());
            currentUser.setAddress(updatedUser.getAddress());

            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    // Lưu referesh token vào database
    public void updateUserRefreshToken(String token, String email) {
        User currentUser = this.handleFetchUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }
}