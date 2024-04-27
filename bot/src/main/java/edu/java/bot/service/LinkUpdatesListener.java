package edu.java.bot.service;

import edu.java.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkUpdatesListener {
    private final UpdatesService updatesService;

    @RetryableTopic(attempts = "2", dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    @KafkaListener(topics = "${app.kafka-updates-consumer-config.topic-name}")
    public void listen(LinkUpdateRequest update) {
        log.info("Link update received. Link URL: {}", update.url());
        updatesService.sendLinkUpdate(update.description(), update.tgChatIds());
    }

    @DltHandler
    public void handleDlt(LinkUpdateRequest update) {
        log.info("Invalid link update received at DLQ");
    }
}
