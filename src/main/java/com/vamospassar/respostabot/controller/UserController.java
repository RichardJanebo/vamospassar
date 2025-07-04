package com.vamospassar.respostabot.controller;


import com.vamospassar.respostabot.dto.FreeTimeDto;
import com.vamospassar.respostabot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping("findFreeDay")
    public ResponseEntity<?> findFreeDay(){
        FreeTimeDto freeTime = userService.findFreeTime();

        return ResponseEntity.ok().body(freeTime);

    }







}


