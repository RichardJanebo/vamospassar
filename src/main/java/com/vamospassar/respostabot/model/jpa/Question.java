package com.vamospassar.respostabot.model.jpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Lob
    private String question;

    @Lob
    private String response;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @OneToMany
    private List<Alternative> alternatives = new ArrayList<>();

    private Question(String question, String response) {
        this.question = question;
        this.response = response;
    }

    public Question() {
    }

    private LocalDateTime createAt;
    private LocalDateTime updateAt;


    @PrePersist
    public void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public static Builder builder() {
        return new Builder();
    }


    public String getResponse() {
        return response;
    }

    public String getQuestion() {
        return question;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class Builder {
        private String question;
        private String response;

        public Builder question(String question) {
            this.question = question;
            return this;
        }

        public Builder response(String response) {
            this.response = response;
            return this;
        }

        public Question build() {
            return new Question(question, response);
        }
    }
}
