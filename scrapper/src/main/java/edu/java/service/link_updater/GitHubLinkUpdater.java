package edu.java.service.link_updater;

import edu.java.domain.model.Link;
import edu.java.responses.GitHubResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GitHubLinkUpdater implements LinkUpdater<GitHubResponse> {

    @Override
    public List<GitHubResponse> getUpdatesForLink(Link link) {
        return null;
    }

    @Override
    public void sendUpdatesToBot(Link link, List<GitHubResponse> updates) {

    }
}
