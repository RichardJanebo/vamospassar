package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.contract.QuestionAnswerService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AiQuestionAnswerService implements QuestionAnswerService {
    private ChatLanguageModel chatLanguageModel;

    private Environment env;

    public AiQuestionAnswerService(ChatLanguageModel chatLanguageModel,Environment env){
        this.chatLanguageModel = chatLanguageModel;
        this.env = env;
    }

    public String findQuestion(Map<String, Map<String, String>> question) {

        PromptTemplate promptTemplate = PromptTemplate.from(
                "Estou realizando uma prova e preciso da sua ajuda com a resposta. " +
                        "Por favor, só me retorne a resposta, não quero explicação nem nada do tipo, apenas a resposta. " +
                        "Essa é a minha pergunta: {{question}}. " +
                        "Alternativas: {{alternatives}}"
        );


        String questionText = question.keySet().stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mapa de pergunta está vazio"));


        Map<String, String> alternativesMap = question.get(questionText);


        String alternativesText = alternativesMap.entrySet().stream()
                .map(e -> e.getKey() + ") " + e.getValue())
                .collect(Collectors.joining(" - "));

        Map<String, Object> variables = new HashMap<>();
        variables.put("question", questionText);
        variables.put("alternatives", alternativesText);


        Prompt prompt = promptTemplate.apply(variables);

        try {

            return chatLanguageModel.generate(prompt.text());
        } catch (Exception e) {
            e.printStackTrace();
            return "Desculpe, não consegui processar sua pergunta no momento.";
        }
    }



}
