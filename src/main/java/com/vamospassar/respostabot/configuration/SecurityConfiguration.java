package com.vamospassar.respostabot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    private final SecurityFilter securityFilter;
    private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    public SecurityConfiguration(SecurityFilter securityFilter , Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler) {
        this.securityFilter = securityFilter;
        this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions().sameOrigin())
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
//                                .requestMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")
                                .requestMatchers("/fonts/**", "/style/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                                .requestMatchers(HttpMethod.POST, "/authentication/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/authentication/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/register").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/webhook/kiwify").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(auth -> auth
                        .loginPage("/login")
                        .successHandler(oauth2AuthenticationSuccessHandler))
                .exceptionHandling(auth -> auth
                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/login")))
                .build();


    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authentication) throws Exception {
        return authentication.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
