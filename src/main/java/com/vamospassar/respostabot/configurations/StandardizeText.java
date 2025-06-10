package com.vamospassar.respostabot.configurations;

import org.springframework.stereotype.Component;

@Component
public class StandardizeText {
    public String standardizeText(String text){
        return text.trim()
                .toUpperCase()
                .replaceAll("[^\\p{L}\\p{Nd}\\s]", "")
                .replaceAll("\\s+", " ");
    }
}
