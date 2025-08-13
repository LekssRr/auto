package com.example.auto.config.kafka;

import com.example.auto.dto.kafka.CreateAutoToClient;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
//    @Value("${spring.kafka.bootstrapServers}")
//    private String bootstrapServers;
//
//    @Value("${kafka.max.in.request.rep.connection}")
//    private int maxInFlightRequestPerConnection;
//
//    @Value("${kafka.name.default.topic.credit}")
//    private String nameDefaultTopic;
//
//    @Bean
//    public ProducerFactory<String, CreateAutoToClient> multiEventProducerFactoryAccountToCard() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        config.put(ProducerConfig.ACKS_CONFIG, "all");
//        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
//        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestPerConnection);
//
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    @Bean
//    public KafkaTemplate<String, CreateAutoToClient> multiEventKafkaTemplateToCard() {
//        KafkaTemplate<String, CreateAutoToClient> template = new KafkaTemplate<>(multiEventProducerFactoryAccountToCard());
//        template.setDefaultTopic(nameDefaultTopic);
//        return template;
//    }
}
