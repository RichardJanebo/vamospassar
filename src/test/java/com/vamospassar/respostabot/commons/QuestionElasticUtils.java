package com.vamospassar.respostabot.commons;

import com.vamospassar.respostabot.model.elastic.QuestionDocument;
import com.vamospassar.respostabot.model.jpa.Question;
import com.vamospassar.respostabot.model.jpa.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class QuestionElasticUtils {

    private final UserUtils utils = new UserUtils();


    public QuestionDocument getQuestionDocumentTest() {
        User userTest = utils.createUserTest();
        QuestionDocument question = new QuestionDocument();
                question.setQuestion("Qual a capital da França?");
                question.setResponse("Paris");

        return question;
    }

    public List<QuestionDocument> getListQuestionDocumentTest() {
        User userTest = utils.createUserTest();

        QuestionDocument q1 = new QuestionDocument();
        q1.setQuestion("Qual é o maior planeta do sistema solar?");
        q1.setResponse("Júpiter");

        QuestionDocument q2 = new QuestionDocument();
        q2.setQuestion("Quem pintou a Mona Lisa?");
        q2.setResponse("Leonardo da Vinci");

        QuestionDocument q3 = new QuestionDocument();
        q3.setQuestion("Qual é o elemento químico representado por H?");
        q3.setResponse("Hidrogênio");

        QuestionDocument q4 = new QuestionDocument();
        q4.setQuestion("Quantos continentes existem na Terra?");
        q4.setResponse("Sete");

        QuestionDocument q5 = new QuestionDocument();
        q5.setQuestion("Em que ano o homem pisou na Lua pela primeira vez?");
        q5.setResponse("1969");

        QuestionDocument q6 = new QuestionDocument();
        q6.setQuestion("Qual a capital do Japão?");
        q6.setResponse("Tóquio");

        return List.of(q1, q2, q3, q4, q5, q6);
    }

}
