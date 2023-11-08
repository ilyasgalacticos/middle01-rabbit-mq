package kz.bitlab.rabbit.rabbittest.sender;

import kz.bitlab.rabbit.rabbittest.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.order.topic.exchange}")
    private String topicExchange;

    @Value("${mq.order.fanout.exchange}")
    private String fanoutExchange;

    public void sendOrderToPrepare(OrderDTO orderDTO, String region){
        String routingKey = "order." + region;
        rabbitTemplate.convertAndSend(topicExchange, routingKey, orderDTO);
    }

    public void updateOrderStatus(OrderDTO orderDTO, String status){
        orderDTO.setStatus(status);
        rabbitTemplate.convertAndSend(fanoutExchange, "", orderDTO);
    }

}
