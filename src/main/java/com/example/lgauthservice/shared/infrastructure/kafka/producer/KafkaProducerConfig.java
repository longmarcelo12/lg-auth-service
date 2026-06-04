//package com.example.lgauthservice.shared.infrastructure.kafka.producer;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.Map;
//
///**
// * Cấu hình Kafka Producer sử dụng KafkaProducerProperties.
// */
//@Configuration(proxyBeanMethods = false)
//public class KafkaProducerConfig {
//
//    private final KafkaProducerProperties producerProperties;
//
//    @Autowired
//    public KafkaProducerConfig(KafkaProducerProperties producerProperties) {
//        this.producerProperties = producerProperties;
//    }
//
//    /**
//     * @return ProducerFactory<String, String>
//     */
//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//
//        Map<String, Object> configProps = producerProperties.buildProducerConfigs();
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
//        return new KafkaTemplate<>(producerFactory);
//    }
//}