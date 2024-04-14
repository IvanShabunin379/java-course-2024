package edu.java.service.link_updater;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.domain.model.Link;
import edu.java.domain.model.TgChat;
import edu.java.dto.LinkUpdateRequest;
import edu.java.responses.GitHubResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.java.service.LinksService;
import edu.java.service.TgChatsService;
import org.springframework.stereotype.Service;

@Service
public class GitHubLinkUpdater implements LinkUpdater<GitHubResponse> {
    private record RepoInfo(String owner, String repoName) {
    }

    private static final Pattern GIT_HUB_REPO_URL_PATTERN = Pattern.compile("https://github.com/(\\w+)/(\\w+)");

    private final GitHubClient gitHubClient;
    private final BotClient botClient;
    private final LinksService linksService;
    private final TgChatsService tgChatsService;

    public GitHubLinkUpdater(
        GitHubClient gitHubClient,
        BotClient botClient,
        LinksService linksService,
        TgChatsService tgChatsService
    ) {
        this.gitHubClient = gitHubClient;
        this.botClient = botClient;
        this.linksService = linksService;
        this.tgChatsService = tgChatsService;
    }

    @Override
    public List<GitHubResponse> getUpdatesForLink(Link link) {
        RepoInfo repoInfo = parseRepoInfo(link.url());

        OffsetDateTime currentTimestamp = OffsetDateTime.now();
        List<GitHubResponse> responses =
            gitHubClient.getRepositoryUpdates(
                repoInfo.owner(),
                repoInfo.repoName(),
                link.lastCheckTime(),
                currentTimestamp
            );
        linksService.updateLastCheckTime(link.id(), currentTimestamp);

        return responses;
    }

    @Override
    public void sendUpdatesToBot(Link link, List<GitHubResponse> updates) {
        for (var update : updates) {
            List<Long> tgChatsIds = tgChatsService.listAll(link.url()).stream()
                .map(TgChat::id)
                .toList();

            LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
                link.id(),
                link.url(),
                String.format(
                    "Новый %s в репозитории %s.\n%s",
                    update.activityType(),
                    link.url(),
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
}

