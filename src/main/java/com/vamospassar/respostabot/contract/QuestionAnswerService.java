package com.vamospassar.respostabot.contract;

import java.util.Map;

public interface QuestionAnswerService {
    String findQuestion(Map<String, Map<String, String>> question);
}
