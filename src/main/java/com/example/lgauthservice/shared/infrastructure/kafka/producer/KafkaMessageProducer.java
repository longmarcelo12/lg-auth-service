//package com.example.lgauthservice.shared.infrastructure.kafka.producer;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.KafkaException;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.concurrent.CompletableFuture;
//
//@Service()
//public class KafkaMessageProducer {
//
//    private static final Logger log = LoggerFactory.getLogger(KafkaMessageProducer.class);
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public KafkaMessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    /**
//     * Gửi tin nhắn đến Kafka và xử lý kết quả không đồng bộ.
//     */
//    public void sendMessage(String topic, String key, String message) {
//
//        // Gửi tin nhắn bất đồng bộ
//        CompletableFuture<SendResult<String, String>> future =
//                kafkaTemplate.send(topic, key, message);
//
//        future.whenComplete((result, ex) -> {
//            if (ex != null) {
//                Object retriesConfig = getRetriesConfig();
//                log.error("Gửi tin nhắn đến topic {} sau {} lần retry thất bại: {}",
//                        topic,
//                        retriesConfig,
//                        ex.getMessage(),
//                        ex);
//
//                // Xử lý message thất bại
//                handleFailedMessage(topic, key, message, ex);
//            } else {
//                // Gửi thành công
//                log.info("✅ Gửi tin nhắn thành công tới topic: {} | Partition: {} | Offset: {} | Timestamp: {}",
//                        topic,
//                        result.getRecordMetadata().partition(),
//                        result.getRecordMetadata().offset(),
//                        Instant.ofEpochMilli(result.getRecordMetadata().timestamp()));
//            }
//        });
//    }
//
//    private Object getRetriesConfig() {
//        try {
//            return kafkaTemplate.getProducerFactory()
//                    .getConfigurationProperties()
//                    .get("retries");
//        } catch (KafkaException | NullPointerException e) {
//            return "N/A (Lỗi đọc config retries)";
//        }
//    }
//
//    /**
//     * Logic xử lý khi tin nhắn thất bại hoàn toàn (sau khi retry).
//     */
//    @Async(value = "commonAsyncExecutor")
//    protected void handleFailedMessage(String topic, String key, String message, Throwable ex) {
//        log.warn("Done handle fail Message. Topic: {}, Key: {}", topic, key);
//    }
//}
//
