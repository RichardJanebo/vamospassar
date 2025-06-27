package com.vamospassar.respostabot.dto.questions;

import java.util.Map;

public record QuestionDto(String question, Map<String,String> alternatives){
}
