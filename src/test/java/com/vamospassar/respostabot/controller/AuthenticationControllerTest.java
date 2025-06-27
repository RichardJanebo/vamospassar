package com.vamospassar.respostabot.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vamospassar.respostabot.configuration.SecurityFilter;
import com.vamospassar.respostabot.dto.LoginResponseDto;
import com.vamospassar.respostabot.dto.UserLoginRequestDto;
import com.vamospassar.respostabot.dto.UserRegisterPostDto;
import com.vamospassar.respostabot.mapper.UserMapper;
import com.vamospassar.respostabot.model.jpa.User;
import com.vamospassar.respostabot.repository.jpa.UserRepository;
import com.vamospassar.respostabot.service.AuthorizationService;
import com.vamospassar.respostabot.service.TokenService;
import com.vamospassar.respostabot.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AuthenticationController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityFilter securityFilter;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Should register user when data is valid")
    @Order(1)
    @Test
    void registerUser_when_valid() throws Exception {
        UserRegisterPostDto userDto = new UserRegisterPostDto(
                "test@gmail.com",
                "123456",
                "(67)99999-9999",
                "Test User"
        );

        User user = User.builder()
                .email("test@gmail.com")
                .password("123456")
                .name("Test User")
                .cell("(67)99999-9999")
                .build();

        Mockito.when(userMapper.userRegisterToUser(userDto)).thenReturn(user);

        mockMvc.perform(post("/authentication/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario criado com sucessso"));
    }

    @DisplayName("Should login when credentials are valid and return token")
    @Order(2)
    @Test
    void login_when_credentials_are_valid() throws Exception {
        UserLoginRequestDto loginDto = new UserLoginRequestDto("test@gmail.com", "123456");

        User user = User.builder()
                .email("test@gmail.com")
                .password("123456")
                .name("Test User")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        Mockito.when(tokenService.generateToken(any(User.class)))
                .thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk());
    }
}
