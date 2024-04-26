package edu.java.service.link_updates_sender;

import edu.java.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer {
    private final KafkaTemplate<Long, LinkUpdateRequest> linkUpdatesProducer;

    public void sendLinkUpdate(LinkUpdateRequest update) {
        try {
            linkUpdatesProducer.sendDefault(update).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error occurred during sending to Kafka", e);
        }
    }
}
