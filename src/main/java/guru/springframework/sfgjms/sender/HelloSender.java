package guru.springframework.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
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
  private final ObjectMapper objectMapper;

  @Scheduled(fixedRate = 2000)
  public void sendMessage() {
//    log.info("I'm sending a message");

    HelloWorldMessage message =
        HelloWorldMessage.builder().id(UUID.randomUUID()).message("Hello World!").build();

    jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);

//    log.info("Message sent");
  }

  @Scheduled(fixedRate = 2000)
  public void sendAndReceiveMessage() throws JMSException {

    HelloWorldMessage message =
        HelloWorldMessage.builder().id(UUID.randomUUID()).message("Hello").build();

    Message receivedMessage =
        jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE,
                session -> {
                  Message helloMessage = null;

                  try {
                    helloMessage =
                        session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty(
                        "_type", "guru.springframework.sfgjms.model.HelloWorldMessage");
                    log.info("Sending hello");

                    return helloMessage;

                  } catch (JsonProcessingException ex) {
                    throw new JMSException("boom");
                  }
                });

    log.info("Message received: {}", receivedMessage.getBody(String.class));
  }
}
