package com.vamospassar.respostabot.model.elastic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Objects;

@Document(indexName = "questions")
public class QuestionDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String question;

    @Field(type = FieldType.Text)
    private String response;

    // Getters e setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof QuestionDocument)) return false;

        QuestionDocument that = (QuestionDocument) o;

        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getQuestion(), that.getQuestion()) &&
                Objects.equals(this.getResponse(), that.getResponse());


    }

    @Override
    public int hashCode(){
        return Objects.hash(id,question,response);
    }
}
