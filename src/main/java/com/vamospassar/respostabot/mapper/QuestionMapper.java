package com.vamospassar.respostabot.mapper;

import com.vamospassar.respostabot.configuration.StandardizeText;
import com.vamospassar.respostabot.dto.questions.QuestionPostDto;
import com.vamospassar.respostabot.model.elastic.QuestionDocument;
import com.vamospassar.respostabot.model.jpa.Question;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionMapper {
    private final  StandardizeText standardizeText;

    public QuestionMapper(StandardizeText standardizeText){
        this.standardizeText = standardizeText;
    }
    public List<Question> listQuestionPostDtoToQuestion(List<QuestionPostDto> questionPostDto) {
        List<Question> myQuestions = new ArrayList<>();
        for (int i = 0; i < questionPostDto.size(); i++) {
            QuestionPostDto questionPost = questionPostDto.get(i);

            myQuestions.add(Question.builder()
                    .question(standardizeText.standardizeText(questionPost.question()))
                    .response(standardizeText.standardizeText(questionPost.response()))
                    .build());

        }

        return myQuestions;

    }

    public Question questionPostDtoToQuestion(QuestionPostDto questionPostDto) {
        return Question.builder()
                .question(standardizeText.standardizeText(questionPostDto.question()))
                .response(standardizeText.standardizeText(questionPostDto.response()))
                .build();


    }

    public QuestionDocument toDocument(Question question) {
        QuestionDocument doc = new QuestionDocument();
        doc.setId(question.getId().toString());
        doc.setQuestion(question.getQuestion());
        doc.setResponse(question.getResponse());
        return doc;
    }


}
