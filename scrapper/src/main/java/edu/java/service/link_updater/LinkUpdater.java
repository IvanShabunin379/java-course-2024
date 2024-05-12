package edu.java.service.link_updater;

import edu.java.domain.model.jdbc.Link;
import java.util.List;

public interface LinkUpdater<T> {
    List<T> getUpdatesForLink(Link link);

    void sendUpdatesToBot(Link link, List<T> updates);
}
