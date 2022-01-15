package kr.springboot.dcinside.cartoon.auth.messaging;

import kr.springboot.dcinside.cartoon.auth.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class KafkaLogProducer {

    private final String AUTH_TOPIC = "carbtoon.auth";

    private KafkaTemplate<String, Message> kafkaTemplate;

    public KafkaLogProducer() {
        this.kafkaTemplate = new KafkaConfig().kafkaTemplate();
    }

    @Async
    public void send(Object payload) {
        Message<Object> message =
                MessageBuilder
                        .withPayload(payload)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString())
                        .build();
        kafkaTemplate.send(AUTH_TOPIC, message);
    }

}
