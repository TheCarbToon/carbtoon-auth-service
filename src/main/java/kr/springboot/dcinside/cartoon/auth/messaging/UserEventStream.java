package kr.springboot.dcinside.cartoon.auth.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface UserEventStream {

    String OUTPUT = "cartoonUserChanged";

    /**
     * Output Deprecated -> UserEventKafkaStream으로 변경
     * @return
     */
    @Output(OUTPUT)
    MessageChannel cartoonUserChanged();

}
