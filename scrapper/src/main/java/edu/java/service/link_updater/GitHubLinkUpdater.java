package edu.java.service.link_updater;

import edu.java.domain.model.TgChat;
import edu.java.responses.GitHubResponse;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class GitHubLinkUpdater implements LinkUpdater<GitHubResponse> {

    @Override
    public List<GitHubResponse> getUpdatesForLink(URI url, OffsetDateTime toTimestamp) {
        return null;
    }

    @Override
    public void sendUpdatesToBot(List<GitHubResponse> updates, List<TgChat> tgChats) {

    }
}
