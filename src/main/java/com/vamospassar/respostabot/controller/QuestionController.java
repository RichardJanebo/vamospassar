package com.vamospassar.respostabot.controller;

import com.vamospassar.respostabot.configuration.StandardizeText;
import com.vamospassar.respostabot.dto.questions.QuestionDto;
import com.vamospassar.respostabot.model.elastic.QuestionDocument;
import com.vamospassar.respostabot.model.jpa.Question;
import com.vamospassar.respostabot.repository.elastic.QuestionElasticRepository;
import com.vamospassar.respostabot.repository.jpa.QuestionRepository;
import com.vamospassar.respostabot.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/")
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final StandardizeText standardizeText;
    private final QuestionElasticRepository questionElasticRepository;

    public QuestionController(QuestionService questionService,QuestionElasticRepository questionElasticRepository,QuestionRepository questionRepository,StandardizeText standardizeText) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
        this.standardizeText = standardizeText;
        this.questionElasticRepository = questionElasticRepository;
    }


    @PostMapping("get_response")
    public ResponseEntity<?> responseQuestion(@RequestBody List<QuestionDto> questions) {
        try {

            Map<String, List<String>> response = new HashMap<>();

            List<String> questionByStatement = questionService.findQuestionByStatement(questions);


            response.put("Resposta", questionByStatement);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    public  record Testt(String quess){}

    @PostMapping("teste-q")
    public ResponseEntity<?>teste(@RequestBody Testt testt){
        try {
            System.out.println(testt);
        Question byQuestion = questionRepository.findByQuestion(standardizeText.standardizeText(testt.quess)).orElseThrow(()-> new RuntimeException("Questao não encontrada"));
        return  ResponseEntity.ok().body(byQuestion.getResponse());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/elastic")
    public List<QuestionDocument> getAllQuestionsFromElastic() {
        return questionService.listAllQuestionsFromElastic();
    }
}
