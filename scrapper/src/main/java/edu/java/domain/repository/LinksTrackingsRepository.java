package edu.java.domain.repository;

import edu.java.domain.model.Link;
import edu.java.domain.model.LinkTracking;
import edu.java.domain.model.TgChat;
import java.util.List;

public interface LinksTrackingsRepository {
    void add(LinkTracking linkTracking);

    boolean remove(LinkTracking linkTracking);

    List<Link> findAllLinksByTgChat(long tgChatId);

    List<TgChat> findAllTgChatsByLink(long linkId);
}
