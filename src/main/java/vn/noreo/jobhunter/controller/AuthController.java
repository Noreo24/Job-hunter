package vn.noreo.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.noreo.jobhunter.domain.User;
import vn.noreo.jobhunter.domain.dto.LoginDTO;
import vn.noreo.jobhunter.domain.dto.ResLoginDTO;
import vn.noreo.jobhunter.service.UserService;
import vn.noreo.jobhunter.util.SecurityUtil;
import vn.noreo.jobhunter.util.annotation.ApiMessage;
import vn.noreo.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginRequest) {

        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());

        // Xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Lưu thông tin vào SecurityContextHolder (Có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User currentUser = this.userService.handleFetchUserByUsername(loginRequest.getUsername());
        if (currentUser != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUser.getId(),
                    currentUser.getEmail(),
                    currentUser.getName());
            resLoginDTO.setUser(userLogin);
        }

        // Create access token
        String accessToken = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUser());
        resLoginDTO.setAccessToken(accessToken);

        // Create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(loginRequest.getUsername(), resLoginDTO);
        // Update refresh token to database
        this.userService.updateUserRefreshToken(refreshToken, loginRequest.getUsername());
        // Không lưu access token vào database vì trong db không có access token và ...

        // Set cookie
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true) // Cho phép cookie được truy cập từ http (server), k cho phép truy cập từ js
                .secure(true) // Chỉ gửi cookie qua https, k gửi qua http
                .path("/") // Đường dẫn cookie, sử dụng với tất cả các request trong dự án
                .maxAge(refreshTokenExpiration) // Thời gian sống của cookie, ở đây = thời gian sống của refresh token
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
    }

    // Lấy thông tin tài khoản
    @GetMapping("/auth/account")
    @ApiMessage("Fetch account information")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUser = this.userService.handleFetchUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setEmail(currentUser.getEmail());
            userLogin.setUsername(currentUser.getName());
        }
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Refresh access token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refreshToken", defaultValue = "noCookies") String refreshToken)
            throws IdInvalidException {

        // Check cookies exist
        if (refreshToken.equals("noCookies")) {
            throw new IdInvalidException("You don't have refresh token in cookies!");
        }

        // Check refresh token
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();

        // Check user exist by email & refresh token
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new IdInvalidException("Refresh token is invalid");
        }

        // Create new access token/set refresh token cookies
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getName());
        resLoginDTO.setUser(userLogin);

        // Create access token
        String accessToken = this.securityUtil.createAccessToken(email, resLoginDTO.getUser());
        resLoginDTO.setAccessToken(accessToken);

        // Create refresh token
        String newRefreshToken = this.securityUtil.createRefreshToken(email, resLoginDTO);
        // Update refresh token to database
        this.userService.updateUserRefreshToken(newRefreshToken, email);
        // Không lưu access token vào database vì trong db không có access token và ...

        // Set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true) // Cho phép cookie được truy cập từ http (server), k cho phép truy cập từ js
                .secure(true) // Chỉ gửi cookie qua https, k gửi qua http
                .path("/") // Đường dẫn cookie, sử dụng với tất cả các request trong dự án
                .maxAge(refreshTokenExpiration) // Thời gian sống của cookie, ở đây = thời gian sống của refresh token
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
    }
}
