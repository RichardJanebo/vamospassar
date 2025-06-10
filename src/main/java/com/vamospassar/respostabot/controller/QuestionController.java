package com.vamospassar.respostabot.controller;

import com.vamospassar.respostabot.dto.questions.QuestionDto;
import com.vamospassar.respostabot.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }


    @PostMapping("get_response")
    public ResponseEntity<?> responseQuestion(@RequestBody List<QuestionDto> questions) {
        List<String> perguntas = new ArrayList<>();

        for (QuestionDto q : questions) {
            perguntas.add(q.question());
            System.out.println("Perguntas " + q);
        }


        Map<String, List<String>> response = new HashMap<>();

        List<String> questionByStatement = questionService.findQuestionByStatement(perguntas);

//        List<String> questionByStatement = List.of("1A","2B","3C","4C","5A");
        response.put("Resposta", questionByStatement);
        return ResponseEntity.ok(response);
    }
}
