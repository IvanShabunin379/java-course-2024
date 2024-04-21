package edu.java.service;

import edu.java.domain.model.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinksService {
    Link add(long tgChatId, URI url);

    Link remove(long tgChatId, URI url);

    void updateLastCheckTime(long id, OffsetDateTime lastCheckTime);

    List<Link> listAll(long tgChatId);

    List<Link> findUncheckedLinksForLongestTime(int limit);
}
