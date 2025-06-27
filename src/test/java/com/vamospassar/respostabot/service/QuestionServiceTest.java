package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.commons.QuestionElasticUtils;
import com.vamospassar.respostabot.commons.QuestionUtils;
import com.vamospassar.respostabot.commons.UserUtils;
import com.vamospassar.respostabot.configuration.StandardizeText;
import com.vamospassar.respostabot.dto.questions.QuestionDto;
import com.vamospassar.respostabot.exception.UserNotFoundException;
import com.vamospassar.respostabot.model.elastic.QuestionDocument;
import com.vamospassar.respostabot.model.jpa.Question;
import com.vamospassar.respostabot.model.jpa.User;
import com.vamospassar.respostabot.repository.elastic.QuestionElasticRepository;
import com.vamospassar.respostabot.repository.jpa.QuestionRepository;
import com.vamospassar.respostabot.repository.jpa.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserService userService;

    @Mock
    private QuestionElasticRepository questionElasticRepository;

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuestionUtils questionUtils;

    @InjectMocks
    private QuestionElasticUtils questionElasticUtils;

    @InjectMocks
    private UserUtils userUtils;



    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Mock
    private  StandardizeText standardizeText = new StandardizeText();


    @Order(1)
    @DisplayName("Should save all Question when successful")
    @Test
    void saveAllQuestion_when_successFull() {
        User userTest = userUtils.createUserTest();


        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userTest.getEmail(), null, userTest.getAuthorities());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<Question> questions = questionUtils.getListQuestionTest();

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(userTest));
        when(questionRepository.saveAll(anyList())).thenReturn(questions);


        questionService.saveAllQuestion(questions);


        verify(questionRepository).saveAll(anyList());
        verify(questionElasticRepository).saveAll(anyList());
    }

    @Order(2)
    @DisplayName("Should user not found in context")
    @Test
    void userNotFound() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "user@example.com", null, List.of());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());


        assertThrows(UserNotFoundException.class, () -> questionService.saveAllQuestion(List.of()));


        verify(questionRepository, org.mockito.Mockito.never()).saveAll(anyList());
        verify(questionElasticRepository, org.mockito.Mockito.never()).saveAll(anyList());
    }

    @Order(3)
    @DisplayName("Should list all question from elastic when exists")
    @Test
    void listAllQuestionsFromElastic() {
        when(questionElasticRepository.findAll()).thenReturn(questionElasticUtils.getListQuestionDocumentTest());

        List<QuestionDocument> questionDocuments = questionService.listAllQuestionsFromElastic();

        Assertions.assertThat(questionDocuments)
                .isNotEmpty()
                .isNotNull()
                .containsExactlyElementsOf(questionElasticUtils.getListQuestionDocumentTest());

    }

    @Order(4)
    @DisplayName("Should save question when successFull")
    @Test
    void saveQuestion() {
        User userTest = userUtils.createUserTest();
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(userTest));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userTest.getEmail(), null, userTest.getAuthorities());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        Question questionTest = questionUtils.getQuestionTest();
        questionService.saveQuestion(questionTest);

        verify(questionRepository).save(any());

    }

    @Order(5)
    @DisplayName("Should find by statement when user is active")
    @Test
    void findQuestionByStatement() {
        // 🔧 Cria o usuário mockado
        User userTest = userUtils.createUserTest();
        userTest.setQuantityQuestionsRequest(0L);

        // 🔧 Define as perguntas e alternativas
        QuestionDto question1 = new QuestionDto(
                "Qual é o maior planeta do sistema solar?",
                Map.of("A", "Terra", "B", "Júpiter", "C", "Marte", "D", "Saturno")
        );
        QuestionDto question2 = new QuestionDto(
                "Quem descobriu o Brasil?",
                Map.of("A", "Cristóvão Colombo", "B", "Dom Pedro I", "C", "Pedro Álvares Cabral", "D", "Américo Vespúcio")
        );
        QuestionDto question3 = new QuestionDto(
                "Quanto é 2 + 2?",
                Map.of("A", "3", "B", "4", "C", "5", "D", "6")
        );

        List<QuestionDto> questions = List.of(question1, question2, question3);

        // 🔧 Padroniza as perguntas
        String standardized1 = standardizeText.standardizeText(question1.question());
        String standardized2 = standardizeText.standardizeText(question2.question());
        String standardized3 = standardizeText.standardizeText(question3.question());

        // 🔧 Respostas esperadas
        String expectedResponse1 = "Júpiter";
        String expectedResponse2 = "Pedro Álvares Cabral";
        String expectedResponse3 = "4";

        // 🔧 Mock do usuário
        when(userRepository.save(userTest)).thenReturn(userTest);
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(userTest));
        when(userService.userIsActive(userTest.getEmail())).thenReturn(true);

        // 🔧 Mock do Elasticsearch (criando os documentos manualmente)
        QuestionDocument doc1 = new QuestionDocument();
        doc1.setId("id1");
        doc1.setQuestion(standardized1);
        doc1.setResponse(expectedResponse1);

        QuestionDocument doc2 = new QuestionDocument();
        doc2.setId("id2");
        doc2.setQuestion(standardized2);
        doc2.setResponse(expectedResponse2);

        QuestionDocument doc3 = new QuestionDocument();
        doc3.setId("id3");
        doc3.setQuestion(standardized3);
        doc3.setResponse(expectedResponse3);

        // 🔧 Configura os retornos dos mocks
        when(questionElasticRepository.searchByQuestion(standardized1)).thenReturn(List.of(doc1));
        when(questionElasticRepository.searchByQuestion(standardized2)).thenReturn(List.of(doc2));
        when(questionElasticRepository.searchByQuestion(standardized3)).thenReturn(List.of(doc3));

        // 🔧 Mock do contexto de autenticação
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userTest.getEmail(), null, userTest.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // 🚀 Executa o método que está sendo testado
        List<String> result = questionService.findQuestionByStatement(questions);

        // ✅ Valida o resultado
        Assertions.assertThat(result)
                .isNotEmpty()
                .containsExactly(
                        "1 " + expectedResponse1,
                        "2 " + expectedResponse2,
                        "3 " + expectedResponse3
                );
    }



}
