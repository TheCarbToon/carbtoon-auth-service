package kr.springboot.dcinside.cartoon.auth.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class KafkaLogProducer {

    private final String SERVICE_NAME = "AUTH-SERVICE";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final StreamBridge streamBridge;

    public KafkaLogProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Async
    public void send(Object payload) {
        Message<Object> message = MessageBuilder
                .withPayload(payload)
                .setHeader("service_name", SERVICE_NAME)
                .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString())
                .build();
        String jsonMessage = "";
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
        streamBridge.send("carbtoonAuth-out-0", jsonMessage);
    }

}
