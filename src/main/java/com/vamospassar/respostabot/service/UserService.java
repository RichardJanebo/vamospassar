package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.dto.FreeTimeDto;
import com.vamospassar.respostabot.enums.Order;
import com.vamospassar.respostabot.enums.Role;
import com.vamospassar.respostabot.exception.UserAlreadyExistsException;
import com.vamospassar.respostabot.exception.UserNotFoundException;
import com.vamospassar.respostabot.model.jpa.User;
import com.vamospassar.respostabot.repository.jpa.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean userIsActive(String email) {
        User userByEmail = findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (userByEmail.getIsActive()) {
            return true;
        }


        boolean isWithinFreeTrialPeriod = LocalDateTime.now().isBefore(userByEmail.getFinalFreeTime());
        boolean isUnderQuestionLimit = userByEmail.getQuantityQuestionsRequest() < 20;

        if (isWithinFreeTrialPeriod && isUnderQuestionLimit) {
            return true;
        }

        return false;
    }


    public User registerUser(User user) {
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        user.setRole(Role.CUSTOMER);

       return userRepository.save(user);

    }

    public FreeTimeDto findFreeTime() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();

        User user = findByEmail(email).orElseThrow(UserNotFoundException::new);

        LocalDateTime createdAt = user.getCreatedAt();
        LocalDateTime today = LocalDateTime.now();

        long daysPassed = ChronoUnit.DAYS.between(createdAt, today);


//        Cada prova contem 5 questoes então para retornar
//           a quantidade de provas realizadas se divide por 5
        int questionByTest = 5;

        Long currentQuantityQuestionsRequest = user.getQuantityQuestionsRequest() / questionByTest;


        FreeTimeDto freeTimeDto = new FreeTimeDto(currentQuantityQuestionsRequest, daysPassed);

        return freeTimeDto;


    }

    public void kiwifyRegisterPayment(String email, String event) {

        User user = findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (Order.ORDER_APPROVED.getValue().equalsIgnoreCase(event)) {
            user.setIsActive(true);
        }

        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
