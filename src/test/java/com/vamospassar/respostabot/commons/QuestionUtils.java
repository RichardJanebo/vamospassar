package com.vamospassar.respostabot.commons;

import com.vamospassar.respostabot.model.jpa.Question;
import com.vamospassar.respostabot.model.jpa.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class QuestionUtils {

    private final UserUtils utils = new UserUtils();


    public Question getQuestionTest() {
        User userTest = utils.createUserTest();
        Question question = Question.builder()
                .question("Qual a capital da França?")
                .response("Paris")
                .build();

        question.setUser(userTest);
        question.setId(UUID.randomUUID());

        return question;
    }

    public List<Question> getListQuestionTest() {
        User userTest = utils.createUserTest();

        Question q1 = Question.builder()
                .question("Qual é o maior planeta do sistema solar?")
                .response("Júpiter")
                .build();

        q1.setId(UUID.randomUUID());

        Question q2 = Question.builder()
                .question("Quem pintou a Mona Lisa?")
                .response("Leonardo da Vinci")
                .build();
        q2.setId(UUID.randomUUID());

        Question q3 = Question.builder()
                .question("Qual é o elemento químico representado por H?")
                .response("Hidrogênio")
                .build();
        q3.setId(UUID.randomUUID());

        Question q4 = Question.builder()
                .question("Quantos continentes existem na Terra?")
                .response("Sete")
                .build();
        q4.setId(UUID.randomUUID());

        Question q5 = Question.builder()
                .question("Em que ano o homem pisou na Lua pela primeira vez?")
                .response("1969")
                .build();
        q5.setId(UUID.randomUUID());
        Question q6 = Question.builder()
                .question("Qual a capital do Japão?")
                .response("Tóquio")
                .build();
        q6.setId(UUID.randomUUID());

        List.of(q1, q2, q3, q4, q5, q6).forEach(q -> q.setUser(userTest));

        return List.of(q1, q2, q3, q4, q5, q6);
    }
}
