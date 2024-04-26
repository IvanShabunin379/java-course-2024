package edu.java.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkUpdatesSenderConfig {
    @Bean
    public boolean useQueue(ScrapperAppConfig config) {
        return config.useQueue();
    }
}
