package com.vamospassar.respostabot.controller;


import com.vamospassar.respostabot.dto.LoginResponseDto;
import com.vamospassar.respostabot.dto.UserLoginRequestDto;
import com.vamospassar.respostabot.dto.UserRegisterPostDto;
import com.vamospassar.respostabot.mapper.UserMapper;
import com.vamospassar.respostabot.model.User;
import com.vamospassar.respostabot.service.TokenService;
import com.vamospassar.respostabot.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@CrossOrigin("*")
@RestController
@RequestMapping("authentication")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final TokenService tokenService;


    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) throws IOException {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userLoginRequestDto.email(), userLoginRequestDto.password());
        Authentication authenticate = authenticationManager.authenticate(usernamePassword);
        User user = (User) authenticate.getPrincipal();
        String token = tokenService.generateToken(user);
        Cookie cookie = new Cookie("token",token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60 * 4);

        response.addCookie(cookie);

        return ResponseEntity.ok(new LoginResponseDto(token,"/dashboard"));

    }

    @PostMapping("register")
    public ResponseEntity registerUser(@RequestBody UserRegisterPostDto userRegisterPostDto) {
        User user = userMapper.userRegisterToUser(userRegisterPostDto);
        userService.registerUser(user);
        return ResponseEntity.ok("Usuario criado com sucessso");
    }


}
