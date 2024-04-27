package edu.java.bot.service;

import edu.java.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkUpdatesListener {
    private final UpdatesService updatesService;

    @KafkaListener(topics = "${app.kafka-updates-consumer-config.topic-name}")
    public void listen(LinkUpdateRequest update) {
        updatesService.sendLinkUpdate(update.description(), update.tgChatIds());
        log.info("Link update received. Link URL: {}", update.url());
    }
}
