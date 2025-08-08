package com.post_hub.utils_service.kafka;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.post_hub.utils_service.entity.ActionLog;
import com.post_hub.utils_service.kafka.model.UtilMessage;
import com.post_hub.utils_service.model.enums.PostioService;
import com.post_hub.utils_service.service.ActionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final ActionLogService actionLogService;

    private static final ObjectMapper UNMARSHALLER = new ObjectMapper();

    static {
        UNMARSHALLER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "iam_topic_for_demo", groupId = "utils_service")
    public void handleIamMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Received message: {}", consumerRecord.value());
        process(consumerRecord, acknowledgment);
    }

    private void process(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        try {
            UtilMessage message = UNMARSHALLER.readValue(consumerRecord.value(), UtilMessage.class);
            ActionLog saved = actionLogService.saveLogFromKafkaMessage(message);
            log.debug("Saved new log {}", saved);
        } catch (Exception e) {
            log.warn(String.format("%s logs consumer has undefined error. Message: %s", "IAM", consumerRecord.value()), e);
        }

        acknowledgment.acknowledge();
    }
}
