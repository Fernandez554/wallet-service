package com.nttbank.microservices.walletservice.util;

import com.nttbank.microservices.commonlibrary.event.GenericEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * KafkaDebitCardUtil class provides utility methods for sending messages to the Kafka topic.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaDebitCardUtil {

  private final KafkaTemplate<String, GenericEvent> kafkaTemplate;

  @Value("${kafka.nttbank.topic.producers[1]}")
  private String topicName;

  /**
   * Sends a message to the Kafka topic.
   */
  public void sendMessage(GenericEvent obj) {
    log.info("Sending message to the topic " + topicName);
    kafkaTemplate.send(topicName, obj);
  }

}
