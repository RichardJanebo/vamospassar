package com.vamospassar.respostabot.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("admin")
public class AdminPageController {

    @GetMapping("dashboard")
    public String adminDashboard(){
        return "admin/dashboard";
    }
}
