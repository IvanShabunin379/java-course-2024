package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.dto.LinkUpdateRequest;
import edu.java.retry.RetryFactory;
import edu.java.service.link_updates_sender.LinkUpdatesSender;
import edu.java.service.link_updates_sender.ScrapperQueueProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class LinkUpdatesSenderConfig {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public LinkUpdatesSender botClient(ScrapperAppConfig config) {
        return new BotClient(RetryFactory.createRetry(config.botClientRetry()));
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public LinkUpdatesSender scrapperQueueProducer(KafkaTemplate<Long, LinkUpdateRequest> linkUpdatesProducer) {
        return new ScrapperQueueProducer(linkUpdatesProducer);
    }
}
