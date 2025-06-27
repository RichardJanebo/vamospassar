package com.vamospassar.respostabot.controller.admin;

import com.vamospassar.respostabot.dto.questions.QuestionPostDto;
import com.vamospassar.respostabot.mapper.QuestionMapper;
import com.vamospassar.respostabot.model.jpa.Question;
import com.vamospassar.respostabot.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {
    private final QuestionMapper questionMapper;
    private final QuestionService questionService;

    public AdminController(QuestionMapper questionMapper, QuestionService questionService) {
        this.questionMapper = questionMapper;
        this.questionService = questionService;
    }

    @PostMapping("save_all_question")
    public ResponseEntity<?> saveAllQuestion(@RequestBody List<QuestionPostDto> questionPostDto) {
        try {
            List<Question> questions = questionMapper.listQuestionPostDtoToQuestion(questionPostDto);
            questionService.saveAllQuestion(questions);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }


    @PostMapping("save_question")
    public ResponseEntity<?> saveQuestion(@RequestBody QuestionPostDto questionPostDto) {
        try {
            Question question = questionMapper.questionPostDtoToQuestion(questionPostDto);
            questionService.saveQuestion(question);
            return ResponseEntity.status(HttpStatus.CREATED).build();


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
