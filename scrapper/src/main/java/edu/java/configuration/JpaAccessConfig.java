package edu.java.configuration;

import edu.java.domain.repository.TgChatsRepository;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import edu.java.service.LinksService;
import edu.java.service.TgChatsService;
import edu.java.service.jpa.JpaLinksService;
import edu.java.service.jpa.JpaTgChatsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {
    @Bean
    public LinksService linksService(
        JpaLinksRepository linksRepository,
        JpaTgChatsRepository tgChatsRepository
    ) {
        return new JpaLinksService(linksRepository, tgChatsRepository);
    }

    @Bean
    public TgChatsService tgChatsService(
        JpaTgChatsRepository tgChatsRepository,
        JpaLinksRepository linksRepository
    ) {
        return new JpaTgChatsService(tgChatsRepository, linksRepository);
    }
}
