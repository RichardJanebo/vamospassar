package com.vamospassar.respostabot.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_alternative")
public class Alternative {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 1)
    private String letter;

    @Lob
    private String description;

    @JoinColumn(name = "question_id", nullable = false)
    @ManyToOne
    private Question question;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public Alternative() {}

    private Alternative(String letter, String description, Question question) {
        this.letter = letter;
        this.description = description;
        this.question = question;
    }

    @PrePersist
    public void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String letter;
        private String description;
        private Question question;

        public Builder letter(String letter) {
            this.letter = letter;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder question(Question question) {
            this.question = question;
            return this;
        }

        public Alternative build() {
            return new Alternative(letter, description, question);
        }
    }
}