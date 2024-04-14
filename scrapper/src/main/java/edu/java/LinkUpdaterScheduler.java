package edu.java;

import edu.java.domain.model.Link;
import edu.java.responses.GitHubResponse;
import edu.java.responses.StackOverflowResponse.StackOverflowAnswerInfo;
import edu.java.service.LinksService;
import java.util.List;
import edu.java.service.link_updater.GitHubLinkUpdater;
import edu.java.service.link_updater.StackOverflowLinkUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import static edu.java.utils.LinkTypeChecker.checkLinkType;

@Slf4j
@Component
public class LinkUpdaterScheduler {
    public static final int BATCH_SIZE = 50;

    private final GitHubLinkUpdater gitHubLinkUpdater;
    private final StackOverflowLinkUpdater stackOverflowLinkUpdater;
    private final LinksService linksService;

    public LinkUpdaterScheduler(GitHubLinkUpdater gitHubLinkUpdater, StackOverflowLinkUpdater stackOverflowLinkUpdater, LinksService linksService) {
        this.gitHubLinkUpdater = gitHubLinkUpdater;
        this.stackOverflowLinkUpdater = stackOverflowLinkUpdater;
        this.linksService = linksService;
    }

    @Scheduled(fixedDelayString = "PT${app.scheduler.interval}")
    public void update() {
        log.info("Update method was invoked.");

        List<Link> uncheckedLinksForLongestTime = linksService.findUncheckedLinksForLongestTime(BATCH_SIZE);

        for (Link link : uncheckedLinksForLongestTime) {
            switch (checkLinkType(link.url())) {
                case GITHUB_REPOSITORY: {
                    List<GitHubResponse> updates = gitHubLinkUpdater.getUpdatesForLink(link);
                    gitHubLinkUpdater.sendUpdatesToBot(link, updates);
                }
                case STACKOVERFLOW_QUESTION: {
                    List<StackOverflowAnswerInfo> updates = stackOverflowLinkUpdater.getUpdatesForLink(link);
                    stackOverflowLinkUpdater.sendUpdatesToBot(link, updates);
                }
            }
        }
    }
}
