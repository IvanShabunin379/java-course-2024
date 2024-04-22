package edu.java.service.link_updater;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.TgChat;
import edu.java.dto.LinkUpdateRequest;
import edu.java.responses.GitHubResponse;
import edu.java.service.LinksService;
import edu.java.service.TgChatsService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.java.utils.LinkTypeChecker.GIT_HUB_REPO_URL_PATTERN;

@Service
@RequiredArgsConstructor
public class GitHubLinkUpdater implements LinkUpdater<GitHubResponse> {
    private final GitHubClient gitHubClient;
    private final BotClient botClient;
    private final LinksService linksService;
    private final TgChatsService tgChatsService;

    @Override
    public List<GitHubResponse> getUpdatesForLink(Link link) {
        RepoInfo repoInfo = parseRepoInfo(link.getUrl());

        OffsetDateTime currentTimestamp = OffsetDateTime.now();
        List<GitHubResponse> responses =
                gitHubClient.getRepositoryUpdates(
                        repoInfo.owner(),
                        repoInfo.repoName(),
                        link.getLastCheckTime(),
                        currentTimestamp
                );
        linksService.updateLastCheckTime(link.getId(), currentTimestamp);

        return responses;
    }

    @Override
    public void sendUpdatesToBot(Link link, List<GitHubResponse> updates) {
        for (var update : updates) {
            List<Long> tgChatsIds = tgChatsService.listAll(link.getUrl()).stream()
                    .map(TgChat::getId)
                    .toList();

            LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
                    link.getId(),
                    link.getUrl(),
                    String.format(
                            "Новый %s в репозитории %s.\n%s",
                            update.activityType(),
                            link.getUrl(),
                            update.timestamp().toString()
                    ),
                    tgChatsIds
            );

            botClient.sendLinkUpdate(linkUpdateRequest);
        }
    }

    private RepoInfo parseRepoInfo(URI url) {
        Matcher matcher = GIT_HUB_REPO_URL_PATTERN.matcher(url.toString());
        matcher.find();
        return new RepoInfo(matcher.group(1), matcher.group(2));
    }

    private record RepoInfo(String owner, String repoName) {
    }
}

