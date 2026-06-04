package com.example.lgauthservice.shared.infrastructure.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class KafkaConsumer<K, V> {

    protected abstract void processMessageAsync(String topic, K key, V message) throws Exception;

    protected void handleFailedMessage(String topic, K key, V message, Throwable ex) {
        log.error("❌ Xử lý message từ topic {} thất bại: {}", topic, ex.getMessage(), ex);
    }

    public void consumeAsync(ConsumerRecord<K, V> record, Acknowledgment ack) {

        String topic   = record.topic();
        K key          = record.key();
        V message      = record.value();

        // ✔️ Xử lý bất đồng bộ
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                processMessageAsync(topic, key, message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("❌ Consumer xử lý message từ topic {} thất bại: {}",
                        topic, ex.getMessage(), ex);

                handleFailedMessage(topic, key, message, ex);
                // ❗Không ACK để Kafka retry
                return;
            }

            log.info("✅ Xử lý thành công: topic={} | Partition={} | Offset={} | Timestamp={}",
                    topic,
                    record.partition(),
                    record.offset(),
                    Instant.ofEpochMilli(record.timestamp())
            );

            if (ack != null) ack.acknowledge();
        });
    }
}
