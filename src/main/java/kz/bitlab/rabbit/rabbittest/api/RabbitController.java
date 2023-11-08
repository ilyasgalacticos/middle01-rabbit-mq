package kz.bitlab.rabbit.rabbittest.api;

import kz.bitlab.rabbit.rabbittest.dto.OrderDTO;
import kz.bitlab.rabbit.rabbittest.sender.MessageSender;
import kz.bitlab.rabbit.rabbittest.sender.OrderPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RabbitController {

    private final MessageSender messageSender;
    private final OrderPublisher orderPublisher;

    @PostMapping(value = "/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        try {
            messageSender.sendMessage(message);
            return new ResponseEntity<>("Message send to RabbitMQ: " + message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/order/publish")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderPublisher.sendOrderToPrepare(orderDTO, "almaty");
            return new ResponseEntity<>("Order created", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Order failed to create", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/notification/publish/{status}")
    public ResponseEntity<String> createNotification(@RequestBody OrderDTO orderDTO,
                                                     @PathVariable(name = "status") String status) {
        try {
            orderPublisher.updateOrderStatus(orderDTO, status);
            return new ResponseEntity<>("Order updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Order failed to update", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}