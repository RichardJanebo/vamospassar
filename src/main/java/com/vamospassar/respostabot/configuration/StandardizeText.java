package com.vamospassar.respostabot.configuration;

import org.springframework.stereotype.Component;

@Component
public class StandardizeText {
    public String standardizeText(String text){
        return text.trim()
                .toUpperCase()
                .replaceAll("[^\\p{L}\\p{Nd}\\s]", " ")  // substitui por espaço
                .replaceAll("\\s+", " ");  // junta múltiplos espaços em um só
    }

}
