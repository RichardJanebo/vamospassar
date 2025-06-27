package com.vamospassar.respostabot.configuration;

import com.vamospassar.respostabot.exception.UserNotFoundException;
import com.vamospassar.respostabot.model.jpa.User;
import com.vamospassar.respostabot.service.TokenService;
import com.vamospassar.respostabot.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class Oauth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder encoder;

    public Oauth2AuthenticationSuccessHandler(TokenService tokenService, UserService userService, @Lazy PasswordEncoder encoder) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {


        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");


        Optional<User> optionalUser = userService.findByEmail(email);

        User user;
        user = optionalUser.orElseGet(() -> userService.registerUser(User.builder()
                .email(email)
                .password(encoder.encode(ThreadLocalRandom.current().toString()))
                .cell("")
                .build()));


        String token = tokenService.generateToken(user);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60 * 4);

        response.addCookie(cookie);
        response.sendRedirect("/dashboard");


    }


}
