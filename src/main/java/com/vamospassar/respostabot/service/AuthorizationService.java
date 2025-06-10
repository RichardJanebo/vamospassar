package com.vamospassar.respostabot.service;

import com.vamospassar.respostabot.model.User;
import com.vamospassar.respostabot.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService implements UserDetailsService {
    private final UserRepository userRepository;

    public  AuthorizationService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byEmail = userRepository.findByEmail(username).orElseThrow(()-> new RuntimeException("Usuario não encontrado"));
        return byEmail;
    }
}
