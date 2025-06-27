package com.vamospassar.respostabot.commons;

import com.vamospassar.respostabot.enums.Role;
import com.vamospassar.respostabot.model.jpa.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    public User createUserTest() {
        return User.builder()
                .email("Teste@gmail.com")
                .name("Teste")
                .cell("256960222")
                .password("teste123")
                .role(Role.CUSTOMER)
                .build();
    }
}
