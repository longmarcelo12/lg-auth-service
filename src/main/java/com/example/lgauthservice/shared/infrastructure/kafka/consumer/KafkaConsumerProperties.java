package com.example.lgauthservice.shared.infrastructure.kafka.consumer;

import com.example.lgauthservice.shared.utils.StringUtils;
import lombok.Data;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.consumer")
@ConditionalOnProperty(
        prefix = "kafka.consumer",
        name = "bootstrap-servers"
)
public class KafkaConsumerProperties {
    private String bootstrapServers;
    private String username;
    private String password;
    private String keyDeserializer;
    private String valueDeserializer;
    private String groupId;
    private Boolean enableAutoCommit;
    private String autoOffsetReset;
    private String loggingTopic;
    private Integer maxPollIntervalMs;
    private Integer sessionTimeoutMs;
    private Integer heartbeatIntervalMs;
    private Integer maxPollRecords;
    private Integer fetchMaxWaitMs;
    private final SaslProperties properties = new SaslProperties();

    @Data
    public static class SaslProperties {
        private final Security security = new Security();
        private final Sasl sasl = new Sasl();

        @Data
        public static class Sasl {
            private String username;
            private String password;
        }

        @Data
        public static class Security {
            private String protocol;
            private String mechanism;
        }
    }

    public Map<String, Object> buildConsumerConfigs() {
        Map<String, Object> configProps = new HashMap<>();

        // Cấu hình cơ bản
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServers());
        if (StringUtils.isNotBlank(getGroupId())) {
            configProps.put(ConsumerConfig.GROUP_ID_CONFIG, getGroupId());
        }
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, getKeyDeserializer());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, getValueDeserializer());

        // Cấu hình commit và offset
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, getEnableAutoCommit());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, getAutoOffsetReset());

        // Cấu hình bảo mật SASL
        String securityProtocol = getProperties().getSecurity().getProtocol();
        String saslMechanism = getProperties().getSecurity().getMechanism();

        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        configProps.put(SaslConfigs.SASL_MECHANISM, saslMechanism);

        String username = getProperties().getSasl().getUsername();
        String password = getProperties().getSasl().getPassword();

        if (username != null && password != null) {
            String jaasConfig = String.format(
                    "%s required username=\"%s\" password=\"%s\";",
                    PlainLoginModule.class.getName(),
                    username,
                    password
            );
            configProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        }
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        configProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMs);
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs);
        return configProps;
    }
}