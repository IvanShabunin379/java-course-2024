package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.pojos.Links;
import edu.java.domain.jooq.tables.pojos.LinksTrackings;
import edu.java.domain.jooq.tables.pojos.TgChats;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.Links.LINKS;
import static edu.java.domain.jooq.tables.LinksTrackings.LINKS_TRACKINGS;
import static edu.java.domain.jooq.tables.TgChats.TG_CHATS;

@Repository
@RequiredArgsConstructor
public class JooqLinksTrackingsRepository {
    private final DSLContext dslContext;

    public boolean add(LinksTrackings linkTracking) {
        try {
            dslContext.insertInto(LINKS_TRACKINGS)
                .values(linkTracking.getTgChatId(), linkTracking.getLinkId())
                .execute();
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean remove(LinksTrackings linkTracking) {
        return dslContext.deleteFrom(LINKS_TRACKINGS)
            .where(LINKS_TRACKINGS.TG_CHAT_ID.eq(linkTracking.getTgChatId()))
            .and(LINKS_TRACKINGS.LINK_ID.eq(linkTracking.getLinkId()))
            .execute() == 1;
    }

    public List<Links> findAllLinksByTgChat(long tgChatId) {
        return dslContext.select(LINKS.fields())
            .from(LINKS_TRACKINGS)
            .join(LINKS).on(LINKS_TRACKINGS.LINK_ID.eq(LINKS.ID))
            .where(LINKS_TRACKINGS.TG_CHAT_ID.eq(tgChatId))
            .fetchInto(Links.class);
    }

    public List<TgChats> findAllTgChatsByLink(long linkId) {
        return dslContext.select(TG_CHATS.fields())
            .from(LINKS_TRACKINGS)
            .join(TG_CHATS).on(LINKS_TRACKINGS.TG_CHAT_ID.eq(TG_CHATS.ID))
            .where(LINKS_TRACKINGS.LINK_ID.eq(linkId))
            .fetchInto(TgChats.class);
    }
}
