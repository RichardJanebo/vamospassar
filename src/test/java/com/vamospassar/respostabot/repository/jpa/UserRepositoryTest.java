package com.vamospassar.respostabot.repository.jpa;

import com.vamospassar.respostabot.commons.UserUtils;
import com.vamospassar.respostabot.model.jpa.User;
import com.vamospassar.respostabot.repository.elastic.QuestionElasticRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
@Import(UserUtils.class)
class UserRepositoryTest {
    @MockBean
    private  QuestionElasticRepository questionElasticRepository;

    private UserRepository userRepository;
    private UserUtils utils;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository, UserUtils utils) {
        this.userRepository = userRepository;
        this.utils = utils;
    }

    @Order(1)
    @DisplayName("Should find user by email when exist")
    @Test
    void findUserByEmail_When_user_exist() {
        User userTest = utils.createUserTest();
        userRepository.save(userTest);

        Assertions.assertThat(userRepository.findByEmail(userTest.getEmail()))
                .isPresent()
                .get()
                .extracting(User::getEmail)
                .isEqualTo(userTest.getEmail());

    }


    @Order(2)
    @DisplayName("Should find user by email when not exist")
    @Test
    void findUserByEmail_When_not_exist(){
        Assertions.assertThat(userRepository.findByEmail("emailNãoExiste@gmail.com")).isEmpty();
    }

}