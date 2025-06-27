package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.configuration.StandardizeText;
import com.vamospassar.respostabot.dto.questions.QuestionDto;
import com.vamospassar.respostabot.exception.FreeTimeException;
import com.vamospassar.respostabot.exception.QuestionNotFoundException;
import com.vamospassar.respostabot.exception.UserNotFoundException;
import com.vamospassar.respostabot.model.elastic.QuestionDocument;
import com.vamospassar.respostabot.model.jpa.Alternative;
import com.vamospassar.respostabot.model.jpa.Question;
import com.vamospassar.respostabot.model.jpa.User;
import com.vamospassar.respostabot.repository.elastic.QuestionElasticRepository;
import com.vamospassar.respostabot.repository.jpa.AlternativeRepository;
import com.vamospassar.respostabot.repository.jpa.QuestionRepository;
import com.vamospassar.respostabot.repository.jpa.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final StandardizeText standardizeText;
    private final UserService userService;
    private final QuestionElasticRepository questionElasticRepository;
    private final AiQuestionAnswerService aiQuestionAnswerService;
    private final AlternativeRepository alternativeRepository;

    public QuestionService(QuestionRepository questionRepository,
                           StandardizeText standardizeText,
                           UserService userService,
                           UserRepository userRepository,
                           AiQuestionAnswerService aiQuestionAnswerService,
                           QuestionElasticRepository questionElasticRepository,
                           AlternativeRepository alternativeRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.standardizeText = standardizeText;
        this.userService = userService;
        this.questionElasticRepository = questionElasticRepository;
        this.aiQuestionAnswerService = aiQuestionAnswerService;
        this.alternativeRepository = alternativeRepository;
    }


    public void saveAllQuestion(List<Question> questions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();

        User user = userService.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        for (Question question : questions) {
            question.setUser(user);
        }

        List<Question> savedQuestions = questionRepository.saveAll(questions);


        List<QuestionDocument> documents = savedQuestions.stream()
                .map(q -> {
                    QuestionDocument doc = new QuestionDocument();
                    doc.setId(q.getId().toString());
                    doc.setQuestion(q.getQuestion());
                    doc.setResponse(q.getResponse());
                    return doc;
                }).toList();

        questionElasticRepository.saveAll(documents);
    }

    public List<QuestionDocument> listAllQuestionsFromElastic() {
        List<QuestionDocument> questions = new ArrayList<>();
        questionElasticRepository.findAll().forEach(questions::add);
        return questions;
    }


    public void saveQuestion(Question question) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        User user = userService.findByEmail(email).orElseThrow(UserNotFoundException::new);

        question.setUser(user);

        questionRepository.save(question);

    }

    public List<String> findQuestionByStatement(List<QuestionDto> questions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();

        User user = userService.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!userService.userIsActive(user.getEmail())) {
            throw new FreeTimeException();
        }


        Long currentQuantityQuestions = user.getQuantityQuestionsRequest();

        List<String> response = new ArrayList<>();

        Map<String, Map<String, String>> questionFull = new LinkedHashMap<>();

        int index = 1;

        for (var item : questions) {
            String question = standardizeText.standardizeText(item.question());

            Map<String, String> alternatives = standardizeAlternatives(item.alternatives());

            Optional<Question> questionByStatement = questionRepository.findByQuestion(question);

            System.out.println(index + " " + question);

            questionFull.put(question, alternatives);

            if (questionByStatement.isPresent()) {
                Question question1 = questionByStatement.orElseThrow(QuestionNotFoundException::new);

                response.add(index + " " + question1.getResponse());

            } else {
                String responseIa = aiQuestionAnswerService.findQuestionInAI(questionFull);


                response.add(index + " " + responseIa);


                System.out.println("Passou aqui 01");
                saveQuestionWithAlternatives(user,question,responseIa , alternatives);



                System.out.println("Passou aqui 02");


                questionFull.clear();

                System.out.println("Passou aqui 03");
            }

            index++;
        }


        user.setQuantityQuestionsRequest(currentQuantityQuestions + questions.size());
        userRepository.save(user);

        return response;
    }


    private Map<String, String> standardizeAlternatives(Map<String, String> alternatives) {
        return alternatives.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> standardizeText.standardizeText(e.getValue())
                ));
    }


    @Transactional
    public void saveQuestionWithAlternatives(User user,String question, String response, Map<String,String> alternatives){

        System.out.println("Entrou no metodo 01");

        Question questionAfterAi = Question.builder()
                .question(question)
                .response(response)
                .build();

        questionAfterAi.setUser(user);

        Question questionSave = questionRepository.save(questionAfterAi);

        System.out.println("Entrou no metodo 02");


        List<Alternative> listWithAllAlternatives = alternatives.entrySet().stream()
                .map(e -> Alternative.builder()
                        .letter(e.getKey())
                        .description(e.getValue())
                        .question(questionSave).build()).toList();

        System.out.println("Entrou no metodo 03");

        alternativeRepository.saveAll(listWithAllAlternatives);

    }

}



