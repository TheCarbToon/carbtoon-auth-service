package kr.springboot.dcinside.cartoon.auth.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

@Deprecated
public interface UserEventStream {

    String OUTPUT = "cartoonUserChanged";

    /**
     * Output Deprecated -> StreamBridge
     * @return
     */
    @Output(OUTPUT)
    MessageChannel cartoonUserChanged();

}
