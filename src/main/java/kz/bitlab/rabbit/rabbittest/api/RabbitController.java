package kz.bitlab.rabbit.rabbittest.api;

import kz.bitlab.rabbit.rabbittest.sender.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RabbitController {

    private final MessageSender messageSender;

    @PostMapping(value = "/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message){
        try{
            messageSender.sendMessage(message);
            return new ResponseEntity<>("Message send to RabbitMQ: " + message, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Failed to send message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}