package com.vamospassar.respostabot.repository.jpa;

import com.vamospassar.respostabot.enums.Role;
import com.vamospassar.respostabot.model.jpa.Question;
import com.vamospassar.respostabot.model.jpa.User;
import com.vamospassar.respostabot.repository.elastic.QuestionElasticRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.assertj.core.api.Assertions;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
class QuestionRepositoryTest {
    @MockBean
    private QuestionElasticRepository questionElasticRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;


    @Order(1)
    @DisplayName("Should find question by statement when it exists")
    @Test
    void findQuestion_and_return_when_successful() {
        User user=User.builder()
                .email("Teste@gmail.com")
                .name("Teste")
                .cell("256960222")
                .password("teste123")
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);
        User user1 = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("User Not found"));
        Question question = Question.builder()
                .question("Questão teste")
                .response("Resposta teste")
                .build();
        question.setUser(user1);

        questionRepository.save(question);

        Optional<Question> result = questionRepository.findByQuestion("Questão teste");

        Assertions.assertThat(result)
                .isPresent()
                .get()
                .extracting(Question::getQuestion)
                .isEqualTo("Questão teste");

        Assertions.assertThat(result)
                .get()
                .extracting(Question::getResponse)
                .isEqualTo("Resposta teste");

        Assertions.assertThat(result)
                .get()
                .extracting(Question::getUser)
                .isEqualTo(user1);


    }

    @Order(2)
    @DisplayName("Should find question by statement when not exist")
    @Test
    void findQuestion_and_return_empty_when_notFound(){
        Assertions.assertThat(questionRepository.findByQuestion("Questão inesistente")).isEmpty();
    }



}
