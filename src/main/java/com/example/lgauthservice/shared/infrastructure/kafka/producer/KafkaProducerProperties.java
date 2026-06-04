//package com.example.lgauthservice.shared.infrastructure.kafka.producer;
//
//import lombok.Data;
//import org.apache.kafka.clients.CommonClientConfigs;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.config.SaslConfigs;
//import org.apache.kafka.common.security.plain.PlainLoginModule;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * cấu hình Kafka Producer.
// */
//@Data
//@Component
//@ConfigurationProperties(prefix = "kafka.producer")
//public class KafkaProducerProperties {
//
//    private String bootstrapServers;
//    private String username;
//    private String password;
//    private String keySerializer;
//    private String valueSerializer;
//    private Integer retries;
//    private String acks;
//    private Boolean enableIdempotence;
//    private Integer batchSize;
//    private Integer lingerMs;
//    private Integer bufferMemory;
//    private Integer requestTimeoutMs;
//    private Integer deliveryTimeoutMs;
//    private String loggingTopic;
//    private String clientId;
//    private final SaslProperties properties = new SaslProperties();
//
//    @Data
//    public static class SaslProperties {
//        private final Security security = new Security();
//        private final Sasl sasl = new Sasl();
//
//        @Data
//        public static class Sasl {
//            private String username;
//            private String password;
//        }
//
//        @Data
//        public static class Security {
//            private String protocol;
//            private String mechanism;
//        }
//    }
//
//    /**
//     * Xây dựng Map cấu hình ProducerConfig từ các thuộc tính đã ánh xạ.
//     *
//     * @return Map<String, Object>
//     */
//    public Map<String, Object> buildProducerConfigs() {
//        Map<String, Object> configProps = new HashMap<>();
//
//        // Cấu hình cơ bản
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServers());
//        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, getClientId());
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, getKeySerializer());
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, getValueSerializer());
//
//        // Cấu hình Độ tin cậy (EOS)
//        configProps.put(ProducerConfig.RETRIES_CONFIG, getRetries());
//        configProps.put(ProducerConfig.ACKS_CONFIG, getAcks());
//        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, getEnableIdempotence());
//
//        // Cấu hình Hiệu suất
//        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, getBatchSize());
//        configProps.put(ProducerConfig.LINGER_MS_CONFIG, getLingerMs());
//        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, getBufferMemory());
//
//        // Cấu hình Timeout
//        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, getRequestTimeoutMs());
//        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, getDeliveryTimeoutMs());
//
//        // Cấu hình Bảo mật (SASL)
//        String securityProtocol = getProperties().getSecurity().getProtocol();
//        String saslMechanism = getProperties().getSecurity().getMechanism();
//
//        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
//        configProps.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
//
//        String username = getProperties().getSasl().getUsername();
//        String password = getProperties().getSasl().getPassword();
//
//        if (username != null && password != null) {
//            String jaasConfig = String.format(
//                    "%s required username=\"%s\" password=\"%s\";",
//                    PlainLoginModule.class.getName(),
//                    username,
//                    password
//            );
//            configProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
//        }
//
//        return configProps;
//    }
//}
