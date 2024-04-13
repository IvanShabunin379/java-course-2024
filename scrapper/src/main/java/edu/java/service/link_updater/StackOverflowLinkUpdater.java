package edu.java.service.link_updater;

import edu.java.domain.model.TgChat;
import edu.java.responses.StackOverflowResponse.StackOverflowAnswerInfo;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class StackOverflowLinkUpdater implements LinkUpdater<StackOverflowAnswerInfo> {

    @Override
    public List<StackOverflowAnswerInfo> getUpdatesForLink(URI url, OffsetDateTime toTimestamp) {
        return null;
    }

    @Override
    public void sendUpdatesToBot(List<StackOverflowAnswerInfo> updates, List<TgChat> tgChats) {

    }
}
