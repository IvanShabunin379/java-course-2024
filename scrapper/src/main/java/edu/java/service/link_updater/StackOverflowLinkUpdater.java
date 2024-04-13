package edu.java.service.link_updater;

import edu.java.domain.model.Link;
import edu.java.responses.StackOverflowResponse.StackOverflowAnswerInfo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowLinkUpdater implements LinkUpdater<StackOverflowAnswerInfo> {

    @Override
    public List<StackOverflowAnswerInfo> getUpdatesForLink(Link link) {
        return null;
    }

    @Override
    public void sendUpdatesToBot(Link link, List<StackOverflowAnswerInfo> updates) {

    }
}
