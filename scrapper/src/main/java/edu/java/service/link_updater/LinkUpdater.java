package edu.java.service.link_updater;

import edu.java.domain.model.TgChat;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater<T> {
    List<T> getUpdatesForLink(URI url, OffsetDateTime toTimestamp);

    void sendUpdatesToBot(List<T> updates, List<TgChat> tgChats);
}
