package com.vamospassar.respostabot.mapper;

import com.vamospassar.respostabot.dto.UserRegisterPostDto;
import com.vamospassar.respostabot.model.jpa.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @DisplayName("Should map UserRegisterPostDto to User when valid")
    @Order(1)
    @Test
    void userRegisterToUser_when_valid() {

        UserRegisterPostDto userDto = new UserRegisterPostDto(
                "test@gmail.com",
                "123456",
                "(67) 99999-9999",
                "Test User"
        );

        User user = userMapper.userRegisterToUser(userDto);


        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getEmail()).isEqualTo("test@gmail.com");
        Assertions.assertThat(user.getName()).isEqualTo("Test User");
        Assertions.assertThat(user.getNumber_cell()).isEqualTo("(67) 99999-9999");
        Assertions.assertThat(user.getRole().name()).isEqualTo("ADMIN");


        Assertions.assertThat(user.getPassword()).isNotEqualTo("123456");
        Assertions.assertThat(user.getPassword()).startsWith("$2"); // Padrão de hash do BCrypt
    }
}
