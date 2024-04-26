package edu.java.service.link_updates_sender;

import edu.java.client.BotClient;
import edu.java.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdatesSender {
    private final BotClient botClient;
    private final ScrapperQueueProducer scrapperQueueProducer;
    private final boolean useQueue;

    public void send(LinkUpdateRequest update) {
        if (useQueue) {
            scrapperQueueProducer.sendLinkUpdate(update);
        } else {
            botClient.sendLinkUpdate(update);
        }
    }
}
