package edu.java.configuration;

import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.domain.repository.jdbc.JdbcLinksTrackingsRepository;
import edu.java.domain.repository.jdbc.JdbcTgChatsRepository;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import edu.java.service.LinksService;
import edu.java.service.TgChatsService;
import edu.java.service.jdbc.JdbcLinksService;
import edu.java.service.jdbc.JdbcTgChatsService;
import edu.java.service.jpa.JpaLinksService;
import edu.java.service.jpa.JpaTgChatsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    @Bean
    public LinksService linksService(
        JdbcTgChatsRepository tgChatsRepository,
        JdbcLinksRepository linksRepository,
        JdbcLinksTrackingsRepository linksTrackingsRepository
    ) {
        return new JdbcLinksService(linksRepository, tgChatsRepository, linksTrackingsRepository);
    }

    @Bean
    public TgChatsService tgChatsService(
        JdbcTgChatsRepository tgChatsRepository,
        JdbcLinksRepository linksRepository,
        JdbcLinksTrackingsRepository linksTrackingsRepository
    ) {
        return new JdbcTgChatsService(tgChatsRepository, linksRepository, linksTrackingsRepository);
    }
}
