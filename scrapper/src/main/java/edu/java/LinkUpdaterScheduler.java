package edu.java;

import edu.java.domain.model.Link;
import edu.java.service.LinksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class LinkUpdaterScheduler {
    public static final int BATCH_SIZE = 50;

    private final LinksService linksService;

    public LinkUpdaterScheduler(LinksService linksService) {
        this.linksService = linksService;
    }

    @Scheduled(fixedDelayString = "PT${app.scheduler.interval}")
    public void update() {
        log.info("Update method was invoked.");

        List<Link> uncheckedLinksForLongestTime = linksService.findUncheckedLinksForLongestTime(BATCH_SIZE);

        for (Link link : uncheckedLinksForLongestTime) {

        }
    }
}
