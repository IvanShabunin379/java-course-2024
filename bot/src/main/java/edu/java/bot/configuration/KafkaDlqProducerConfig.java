package edu.java.bot.configuration;

import edu.java.dto.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaDlqProducerConfig {
    @Bean
    public NewTopic dlqTopic(BotAppConfig config) {
        return TopicBuilder.name(config.kafkaDlqProducerConfig().topicName())
            .partitions(1)
            .replicas(1)
            .build();
    }

    @Bean
    public ProducerFactory<Long, LinkUpdateRequest> producerFactory(BotAppConfig config) {
        return new DefaultKafkaProducerFactory<>(senderProps(config.kafkaDlqProducerConfig()));
    }

    private Map<String, Object> senderProps(BotAppConfig.KafkaProducerConfig config) {
        var props = new HashMap<String, Object>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(ProducerConfig.LINGER_MS_CONFIG, config.lingerMs());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    @Bean
    public KafkaTemplate<Long, LinkUpdateRequest> dlqKafkaTemplate(
        ProducerFactory<Long, LinkUpdateRequest> producerFactory,
        BotAppConfig config
    ) {
        var kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(config.kafkaDlqProducerConfig().topicName());
        return kafkaTemplate;
    }
}
