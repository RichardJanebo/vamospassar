package com.vamospassar.respostabot.mapper;

import com.vamospassar.respostabot.dto.UserRegisterPostDto;
import com.vamospassar.respostabot.enums.Role;
import com.vamospassar.respostabot.model.jpa.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {
    public User userRegisterToUser(UserRegisterPostDto userRegisterPostDto) {

        return User.builder()
                .email(userRegisterPostDto.email())
                .password(new BCryptPasswordEncoder().encode(userRegisterPostDto.password()))
                .name(userRegisterPostDto.name())
                .cell(userRegisterPostDto.number_cell())
                .role(Role.ADMIN)
                .build();
    }
}
