package kr.springboot.dcinside.cartoon.auth.messaging;

import kr.springboot.dcinside.cartoon.auth.domain.User;
import kr.springboot.dcinside.cartoon.auth.payload.UserEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserEventSender {

    private final UserEventKafkaStream userEventKafkaStream;

    public void sendUserCreated(User user) {
        log.info("sending user created event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.CREATED));
    }

    public void sendUserUpdated(User user) {
        log.info("sending user updated event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.UPDATED));
    }

    public void sendUserUpdated(User user, String oldPicUrl) {
        log.info("sending user updated (profile pic changed) event for user {}",
                user.getUsername());

        UserEventPayload payload = convertTo(user, UserEventType.UPDATED);
        payload.setOldProfilePicUrl(oldPicUrl);

        sendUserChangedEvent(payload);
    }

    private void sendUserChangedEvent(UserEventPayload payload) {

        Message<UserEventPayload> message =
                MessageBuilder
                        .withPayload(payload)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, payload.getId())
                        .build();
        userEventKafkaStream.send(message);

        log.info("user event {} sent to topic {} for user {}",
                message.getPayload().getEventType().name(),
                userEventKafkaStream.getKAFKA_TOPIC(),
                message.getPayload().getUsername());
    }

    private UserEventPayload convertTo(User user, UserEventType eventType) {

        return UserEventPayload
                .builder()
                .eventType(eventType)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getUserProfile().getDisplayName())
                .profilePictureUrl(user.getUserProfile().getProfilePictureUrl()).build();

    }

}
