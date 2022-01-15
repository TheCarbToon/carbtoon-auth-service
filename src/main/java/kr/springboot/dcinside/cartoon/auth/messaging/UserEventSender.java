package kr.springboot.dcinside.cartoon.auth.messaging;

import kr.springboot.dcinside.cartoon.auth.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Deprecated
@RequiredArgsConstructor
@Component
@Slf4j
public class UserEventSender {

//    private final String USER_EVENT_TOPIC = "carbtoon.user.event";

    private final KafkaLogProducer kafkaLogProducer;

    public void sendUserCreated(User user) {

        log.info("sending user created event for user {}", user.getUsername());
//        sendUserChangedEvent(UserEventPayload.convertTo(user, UserEventType.CREATED));
//        kafkaLogProducer.send(KafkaConfig.USER_EVENT_TOPIC, UserEventPayload.convertTo(user, UserEventType.CREATED));

    }

//    private void sendUserChangedEvent(UserEventPayload payload) {
//
//
//
//        log.info("user event {} sent to topic {} for user {}",
//                message.getPayload().getEventType().name(),
//                USER_EVENT_TOPIC,
//                message.getPayload().getUserName());
//
//    }

}
