package com.vamospassar.respostabot.configurations;

import com.vamospassar.respostabot.service.AuthorizationService;
import com.vamospassar.respostabot.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Service
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final AuthorizationService authorizationService;

    public SecurityFilter(TokenService tokenService, AuthorizationService authorizationService) {
        this.tokenService = tokenService;
        this.authorizationService = authorizationService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (getEndpoints(request)) {
            String tokenFromRequest = getTokenFromRequest(request);
            String userEmail = tokenService.validateToken(tokenFromRequest);
            UserDetails user = authorizationService.loadUserByUsername(userEmail);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String url = request.getHeader("Authorization");
        String token = null;

        if (url != null && url.startsWith("Bearer")) {
            token = url.replace("Bearer", "").trim();
        }

        if (request.getCookies() != null && token == null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase("token")) {
                    token = cookie.getValue();
                }
            }
        }

        return token;

    }

    private boolean getEndpoints(HttpServletRequest servletRequest) {
        String endpoint = servletRequest.getRequestURI();
        String[] publicEndpoints = {
                "/login",
                "/register",
                "/api/webhook/kiwify",
                "/authentication/login",
                "/authentication/register",
                "/h2-console/",
                "/style",
                "/js",
                "/images",
                "/fonts"
        };
        return Arrays.stream(publicEndpoints).noneMatch(endpoint::startsWith);
    }


}
