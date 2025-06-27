package com.vamospassar.respostabot.controller;

import org.hibernate.boot.registry.selector.StrategyRegistration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Page {
    @GetMapping("/dashboard")
    public String paginaAssinar() {
        return "dashboard";
    }

    @GetMapping("/login")
    public String paginaLogin(){
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @GetMapping("/loginsuccess")
    public String paginaLoginSuccessFull(){
        return  "redirect:/loginsuccess.html";
    }

    @GetMapping("/loginfail")
    public String paginaLoginFail(){
        return "redirect:/loginfail";
    }
}
