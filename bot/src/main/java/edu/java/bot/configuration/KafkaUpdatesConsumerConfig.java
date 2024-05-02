package edu.java.bot.configuration;

import edu.java.dto.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class KafkaUpdatesConsumerConfig {
    @Bean
    ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest>
    kafkaListenerContainerFactory(
        ConsumerFactory<Long, LinkUpdateRequest> consumerFactory,
        BotAppConfig config
    ) {
        ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(config.kafkaUpdatesConsumerConfig().concurrency());

        return factory;
    }

    @Bean
    public ConsumerFactory<Long, LinkUpdateRequest> consumerFactory(BotAppConfig config) {
        return new DefaultKafkaConsumerFactory<>(consumerProps(config.kafkaUpdatesConsumerConfig()));
    }

    private Map<String, Object> consumerProps(BotAppConfig.KafkaConsumerConfig kafkaConsumerConfig) {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerConfig.bootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfig.groupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfig.autoOffsetReset());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return props;
    }

}
