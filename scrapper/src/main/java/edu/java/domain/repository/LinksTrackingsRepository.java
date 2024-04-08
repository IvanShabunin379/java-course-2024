package edu.java.domain.repository;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.LinkTracking;
import edu.java.domain.model.jdbc.TgChat;
import java.util.List;

public interface LinksTrackingsRepository {
    void add(LinkTracking linkTracking);

    boolean remove(LinkTracking linkTracking);

    List<Link> findAllLinksByTgChat(long tgChatId);

    List<TgChat> findAllTgChatsByLink(long linkId);
}
