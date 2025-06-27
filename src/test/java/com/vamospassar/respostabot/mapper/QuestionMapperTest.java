package com.vamospassar.respostabot.mapper;

import com.vamospassar.respostabot.configuration.StandardizeText;
import com.vamospassar.respostabot.dto.questions.QuestionPostDto;
import com.vamospassar.respostabot.model.elastic.QuestionDocument;
import com.vamospassar.respostabot.model.jpa.Question;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class QuestionMapperTest {

    @Mock
    private StandardizeText standardizeText;

    @InjectMocks
    private QuestionMapper questionMapper;


    @DisplayName("Should map QuestionPostDto to Question when valid")
    @Order(1)
    @Test
    void questionPostDtoToQuestion_when_valid() {

        QuestionPostDto dto = new QuestionPostDto("Qual a capital do Brasil?", "Brasília");

        when(standardizeText.standardizeText("Qual a capital do Brasil?")).thenReturn("QUAL A CAPITAL DO BRASIL");
        when(standardizeText.standardizeText("Brasília")).thenReturn("BRASILIA");

        Question question = questionMapper.questionPostDtoToQuestion(dto);


        Assertions.assertThat(question).isNotNull();
        Assertions.assertThat(question.getQuestion()).isEqualTo("QUAL A CAPITAL DO BRASIL");
        Assertions.assertThat(question.getResponse()).isEqualTo("BRASILIA");
    }


    @DisplayName("Should map list of QuestionPostDto to list of Question when valid")
    @Order(2)
    @Test
    void listQuestionPostDtoToQuestion_when_valid() {

        List<QuestionPostDto> dtos = List.of(
                new QuestionPostDto("Questão 1", "Resposta 1"),
                new QuestionPostDto("Questão 2", "Resposta 2")
        );

        when(standardizeText.standardizeText("Questão 1")).thenReturn("QUESTAO 1");
        when(standardizeText.standardizeText("Resposta 1")).thenReturn("RESPOSTA 1");

        when(standardizeText.standardizeText("Questão 2")).thenReturn("QUESTAO 2");
        when(standardizeText.standardizeText("Resposta 2")).thenReturn("RESPOSTA 2");


        List<Question> questions = questionMapper.listQuestionPostDtoToQuestion(dtos);


        Assertions.assertThat(questions).hasSize(2);

        Assertions.assertThat(questions.get(0).getQuestion()).isEqualTo("QUESTAO 1");
        Assertions.assertThat(questions.get(0).getResponse()).isEqualTo("RESPOSTA 1");

        Assertions.assertThat(questions.get(1).getQuestion()).isEqualTo("QUESTAO 2");
        Assertions.assertThat(questions.get(1).getResponse()).isEqualTo("RESPOSTA 2");
    }


    @DisplayName("Should map Question to QuestionDocument when valid")
    @Order(3)
    @Test
    void toDocument_when_valid() {

        Question question = Question.builder()
                .question("O que é Java?")
                .response("Uma linguagem de programação.")
                .build();
        question.setId(UUID.randomUUID());


        QuestionDocument document = questionMapper.toDocument(question);


        Assertions.assertThat(document).isNotNull();
        Assertions.assertThat(document.getId()).isEqualTo("1");
        Assertions.assertThat(document.getQuestion()).isEqualTo("O que é Java?");
        Assertions.assertThat(document.getResponse()).isEqualTo("Uma linguagem de programação.");
    }
}
