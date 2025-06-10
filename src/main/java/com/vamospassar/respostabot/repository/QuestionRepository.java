package com.vamospassar.respostabot.repository;

import com.vamospassar.respostabot.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    Optional<Question> findByQuestion(String question);
}
