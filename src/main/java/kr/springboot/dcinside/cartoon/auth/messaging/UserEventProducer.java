package kr.springboot.dcinside.cartoon.auth.messaging;

import kr.springboot.dcinside.cartoon.auth.config.KafkaConfig;
import lombok.Getter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UserEventProducer {

    private KafkaTemplate<String, Message> kafkaTemplate;
    private final String KAFKA_TOPIC = "carbtoon.user.create";

    public UserEventProducer() {
        this.kafkaTemplate = new KafkaConfig().kafkaTemplate();
    }

    public void send(Message message) {
        kafkaTemplate.send(KAFKA_TOPIC, message);
    }




}
