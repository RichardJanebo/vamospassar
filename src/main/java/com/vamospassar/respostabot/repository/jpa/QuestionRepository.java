package com.vamospassar.respostabot.repository.jpa;

import com.vamospassar.respostabot.model.jpa.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    Optional<Question> findByQuestion(String question);
}
