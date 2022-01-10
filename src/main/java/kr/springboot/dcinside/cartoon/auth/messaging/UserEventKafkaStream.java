package kr.springboot.dcinside.cartoon.auth.messaging;

import kr.springboot.dcinside.cartoon.auth.config.KafkaProducerConfig;
import lombok.Getter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UserEventKafkaStream {

    private KafkaTemplate<String, Message> kafkaTemplate;
    private final String KAFKA_TOPIC = "cartoonUserChanged";

    public UserEventKafkaStream() {
        this.kafkaTemplate = new KafkaProducerConfig().kafkaTemplate();
    }

    public void send(Message message) {
        kafkaTemplate.send(KAFKA_TOPIC, message);
    }




}
