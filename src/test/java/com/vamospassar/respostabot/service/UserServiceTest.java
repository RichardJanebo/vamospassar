package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.dto.FreeTimeDto;
import com.vamospassar.respostabot.enums.Order;
import com.vamospassar.respostabot.exception.UserAlreadyExistsException;
import com.vamospassar.respostabot.exception.UserNotFoundException;
import com.vamospassar.respostabot.model.jpa.User;
import com.vamospassar.respostabot.repository.jpa.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User userTest;

    @BeforeEach
    void setUp() {
        userTest = User.builder()
                .email("test@gmail.com")
                .isActive(false)
                .build();
        userTest.setCreatedAt(LocalDateTime.now().minusDays(2));

        userTest.setFinaFreelTime(LocalDateTime.now().plusDays(5));

        userTest.setQuantityQuestionsRequest(5L);
    }

    @DisplayName("Should userIsActive and return true when active")
    @org.junit.jupiter.api.Order(1)
    @Test
    void userIsActive_and_return_true_when_active() {
        userTest.setIsActive(true);
        when(userRepository.findByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));

        boolean isActive = userService.userIsActive(userTest.getEmail());

        Assertions.assertThat(isActive).isTrue();
        verify(userRepository, times(1)).findByEmail(userTest.getEmail());
    }

    @DisplayName("Should registerUser when user is valid")
    @org.junit.jupiter.api.Order(2)
    @Test
    void registerUser_when_user_is_valid() {
        when(userRepository.findByEmail(userTest.getEmail())).thenReturn(Optional.empty());

        userService.registerUser(userTest);

        verify(userRepository, times(1)).save(userTest);
    }

    @DisplayName("Should findFreeTime and return true when isFreeTime")
    @org.junit.jupiter.api.Order(3)
    @Test
    void findFreeTime() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userTest.getEmail());
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));

        FreeTimeDto freeTime = userService.findFreeTime();

        Assertions.assertThat(freeTime).isNotNull();
        Assertions.assertThat(freeTime).extracting(FreeTimeDto::day).isEqualTo(2L);
        Assertions.assertThat(freeTime).extracting(FreeTimeDto::trial).isEqualTo(1L);



        verify(userRepository, times(1)).findByEmail(userTest.getEmail());
    }

    @DisplayName("Should registerPayment when payment is valid")
    @org.junit.jupiter.api.Order(4)
    @Test
    void kiwifyRegisterPayment() {
        when(userRepository.findByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));

        userService.kiwifyRegisterPayment(userTest.getEmail(), Order.ORDER_APPROVED.getValue());

        Assertions.assertThat(userTest.getIsActive()).isTrue();
        verify(userRepository, times(1)).save(userTest);
    }

    @DisplayName("Should find user by email and return user when successFull")
    @org.junit.jupiter.api.Order(5)
    @Test
    void findByEmail() {
        when(userRepository.findByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));

        Optional<User> userResult = userService.findByEmail(userTest.getEmail());

        Assertions.assertThat(userResult).isPresent();
        Assertions.assertThat(userResult.get().getEmail()).isEqualTo(userTest.getEmail());

        verify(userRepository, times(1)).findByEmail(userTest.getEmail());
    }
}
