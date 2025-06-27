package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.commons.UserUtils;
import com.vamospassar.respostabot.model.jpa.User;
import com.vamospassar.respostabot.repository.jpa.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;





@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserUtils utils;


    @Order(1)
    @DisplayName("Should load by userName when exist")
    @Test
    void load_by_userName_when_exist(){
        User userTest = utils.createUserTest();
        BDDMockito.when(userRepository.findByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));

        UserDetails userDetails = authorizationService.loadUserByUsername(userTest.getEmail());

        Assertions.assertThat(userDetails).extracting(UserDetails::getUsername).isEqualTo(userTest.getEmail());
    }

    @Order(2)
    @DisplayName("Should load by userName when not exist and throws exception")
    @Test
    void load_by_userName_when_not_exist(){
        Assertions.assertThatException()
                .isThrownBy(() -> authorizationService.loadUserByUsername("usuario@notfound"))
                .isInstanceOf(RuntimeException.class)
                .extracting(e -> e.getMessage())
                .isEqualTo("Usuario não encontrado");

    }

}