package com.vamospassar.respostabot.controller;

import com.vamospassar.respostabot.dto.WebhookPayloadDto;
import com.vamospassar.respostabot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("api")
public class KiwifyWebhookController {
    private  final UserService userService;

    public KiwifyWebhookController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("webhook/kiwify")
    public ResponseEntity requestWehook(@RequestBody WebhookPayloadDto webhookPayloadDto){

        System.out.println("Cliente email " + webhookPayloadDto.getCustomer().getEmail());
        System.out.println("Evento " + webhookPayloadDto.getWebhookEventType());
        userService.kiwifyRegisterPayment(webhookPayloadDto.getCustomer().getEmail(),webhookPayloadDto.getWebhookEventType());


        return ResponseEntity.ok("Evento recebido com sucesso");
    }

}
