package com.example.lgauthservice.shared.infrastructure.kafka.consumer;//package com.msb.de.pipeline.shared.infrastructure.kafka.consumer;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Service;
//@Slf4j
//@Service
//public class LogConsumer extends KafkaConsumer<String, String> {
//    @Override
//    protected void processMessageAsync(String topic, String key, String message) {
//        log.info("💳 Xử lý payment: {}", message);
//
//        if (message.contains("error")) {
//            throw new RuntimeException("Lỗi payment!");
//        }
//    }
//    @KafkaListener(topics = "de-log-topic", groupId = "pipeline-service")
//    public void listen(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        consumeAsync(record, ack);
//    }
//}
