package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.configurations.StandardizeText;
import com.vamospassar.respostabot.model.Question;
import com.vamospassar.respostabot.model.User;
import com.vamospassar.respostabot.repository.QuestionRepository;
import com.vamospassar.respostabot.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final StandardizeText standardizeText;
    private final UserService userService;

    public QuestionService(QuestionRepository questionRepository,
                           StandardizeText standardizeText,
                           UserService userService,
                           UserRepository userRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.standardizeText = standardizeText;
        this.userService = userService;
    }


    public void saveAllQuestion(List<Question> questions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        for (Question question : questions) {
            question.setUser(user);
        }
        questionRepository.saveAll(questions);
    }

    public void saveQuestion(Question question) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        question.setUser(user);

        questionRepository.save(question);

    }

    public List<String> findQuestionByStatement(List<String> questions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Long currentQuantityQuestions = user.getQuantityQuestionsRequest();

        if (!userService.userIsActive(user.getEmail())) {
            throw new RuntimeException("Periodo de testes chegou ao fim sinto muito");
        }


        List<String> response = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question questionFound = questionRepository.findByQuestion(standardizeText.standardizeText(questions.get(i))).orElseThrow(() -> new RuntimeException("Question not found"));
            response.add(i + 1 + " " + questionFound.getResponse());
        }

        user.setQuantityQuestionsRequest(currentQuantityQuestions + questions.size());

        userRepository.save(user);


        return response;
    }
}


