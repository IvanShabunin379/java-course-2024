package edu.java.configuration;

import java.util.HashMap;
import java.util.Map;
import edu.java.dto.LinkUpdateRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaUpdatesProducerConfig {
    @Bean
    public NewTopic topic(ScrapperAppConfig config) {
        return TopicBuilder.name(config.kafkaUpdatesProducer().topicName())
            .partitions(1)
            .replicas(1)
            .build();
    }

    @Bean
    public ProducerFactory<Long, LinkUpdateRequest> producerFactory(ScrapperAppConfig config) {
        return new DefaultKafkaProducerFactory<>(senderProps(config.kafkaUpdatesProducer()));
    }

    private Map<String, Object> senderProps(ScrapperAppConfig.KafkaProducerConfig kafkaProducerConfig) {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfig.bootstrapServers());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfig.acksMode());
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, kafkaProducerConfig.deliveryTimeout());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfig.lingerMs());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfig.batchSize());
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaProducerConfig.maxInFlightPerConnection());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaProducerConfig.enableIdempotence());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    @Bean
    public KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate(
        ProducerFactory<Long, LinkUpdateRequest> producerFactory,
        ScrapperAppConfig config
    ) {
        var kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(config.kafkaUpdatesProducer().topicName());
        return new KafkaTemplate<>(producerFactory);
    }
}
