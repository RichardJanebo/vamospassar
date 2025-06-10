package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.enums.Order;
import com.vamospassar.respostabot.enums.Role;
import com.vamospassar.respostabot.model.User;
import com.vamospassar.respostabot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean userIsActive(String email) {
        User userByEmail = findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (userByEmail.getIsActive()) {
            return true;
        }


        boolean dentroDoPrazoGratuito = LocalDateTime.now().isBefore(userByEmail.getFinalFreeTime());


        boolean dentroDoLimitePerguntas = userByEmail.getQuantityQuestionsRequest() <= 20;


        if (dentroDoPrazoGratuito || dentroDoLimitePerguntas) {
            return true;
        }

        return false;
    }



    public void registerUser(User user) {
        System.out.println("Email do usuario " + user.getEmail());
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Usuario Ja cadastrado");
        }
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);

    }

    public void kiwifyRegisterPayment(String email, String event){

        User user = findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if(Order.ORDER_APPROVED.getValue().equalsIgnoreCase(event)){
            user.setIsActive(true);
        }

        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
