package edu.java.configuration;

import edu.java.domain.repository.jooq.JooqLinksRepository;
import edu.java.domain.repository.jooq.JooqLinksTrackingsRepository;
import edu.java.domain.repository.jooq.JooqTgChatsRepository;
import edu.java.service.LinksService;
import edu.java.service.TgChatsService;
import edu.java.service.jooq.JooqLinksService;
import edu.java.service.jooq.JooqTgChatsService;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfig {
    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    @Bean
    public LinksService linksService(
        JooqLinksRepository linksRepository,
        JooqTgChatsRepository tgChatsRepository,
        JooqLinksTrackingsRepository linksTrackingsRepository
    ) {
        return new JooqLinksService(linksRepository, tgChatsRepository, linksTrackingsRepository);
    }

    @Bean
    public TgChatsService tgChatsService(
        JooqTgChatsRepository tgChatsRepository,
        JooqLinksRepository linksRepository,
        JooqLinksTrackingsRepository linksTrackingsRepository
    ) {
        return new JooqTgChatsService(tgChatsRepository, linksRepository, linksTrackingsRepository);
    }
}
