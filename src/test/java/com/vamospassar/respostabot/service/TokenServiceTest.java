package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.commons.UserUtils;
import com.vamospassar.respostabot.exception.UserNotFoundException;
import com.vamospassar.respostabot.model.jpa.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @InjectMocks
    private TokenService tokenService;

    @InjectMocks
    private UserUtils utils;


    @Order(1)
    @DisplayName("Should generate token when user exist")
    @Test
    void generateToken_when_user_exist() {
        User userTest = utils.createUserTest();

        String token = tokenService.generateToken(userTest);

        Assertions.assertThat(token).isNotNull().isNotEmpty();


    }

    @Order(2)
    @DisplayName("Should validate token when token is valid")
    @Test
    void validateToken_when_token_is_valid() {
        User userTest = utils.createUserTest();
        String token = tokenService.generateToken(userTest);

        System.out.println(token);

        String email = tokenService.validateToken(token);
        Assertions.assertThat(email).isEqualTo(userTest.getEmail());


    }


    @DisplayName("Should validate token when signature is invalid and throws exception")
    @Order(3)
    @Test
    void validateToken_when_signature_is_invalid() {
        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());

        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"sub\":\"fake@test.com\"}".getBytes());

        String signature = "invalidsignature";

        String fakeToken = header + "." + payload + "." + signature;

        Assertions.assertThatException().isThrownBy(() -> tokenService.validateToken(fakeToken))
                .isInstanceOf(RuntimeException.class)
                .extracting(Throwable::getMessage).isEqualTo("Error while verification token ");

    }


    @DisplayName("Should  generate token when user is null and throws exception")
    @Order(4)
    @Test
    void generateToken_when_user_is_null() {
        User userTest = User.builder().build();
        Assertions.assertThatException().isThrownBy(() -> tokenService.generateToken(userTest))
                .isInstanceOf(UserNotFoundException.class);



    }


}