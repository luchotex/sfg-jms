package guru.springframework.sfgjms.sender;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Luis M. Kupferberg Ruiz (lkupferberg@overactive.com)
 * @created 2020-08-02 20:47
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HelloSender {

  private final JmsTemplate jmsTemplate;

  @Scheduled(fixedRate = 2000)
  public void sendMessage() {
    log.info("I'm sending a message");

    HelloWorldMessage message =
        HelloWorldMessage.builder().id(UUID.randomUUID()).message("Hello World!").build();

    jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);

    log.info("Message sent");
  }
}
