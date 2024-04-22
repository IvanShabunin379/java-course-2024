package edu.java.domain.repository.jdbc;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.LinkTracking;
import edu.java.domain.model.jdbc.TgChat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinksTrackingsRepository {
    private final JdbcTemplate jdbcTemplate;

    public boolean add(LinkTracking linkTracking) {
        String sql = """
            INSERT INTO links_trackings(tg_chat_id, link_id)
            VALUES (?, ?)
            """;
          
        try {
            jdbcTemplate.update(sql, linkTracking.getTgChatId(), linkTracking.getLinkId());
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean remove(LinkTracking linkTracking) {
        String sql = """
            DELETE FROM links_trackings WHERE tg_chat_id = ? AND link_id = ?
            """;
        return jdbcTemplate.update(sql, linkTracking.getTgChatId(), linkTracking.getLinkId()) == 1;
    }

    public List<Link> findAllLinksByTgChat(long tgChatId) {
        String sql = """
            SELECT L.*
            FROM links_trackings LT JOIN links L
                ON LT.link_id = L.id
            WHERE LT.tg_chat_id = ?
            """;

        return jdbcTemplate.query(
            sql,
            new Object[] {tgChatId},
            new BeanPropertyRowMapper<>(Link.class)
        );
    }

    public List<TgChat> findAllTgChatsByLink(long linkId) {
        String sql = """
            SELECT T.*
            FROM links_trackings LT JOIN tg_chats T
                ON LT.tg_chat_id = T.id
            WHERE LT.link_id = ?
            """;

        return jdbcTemplate.query(
            sql,
            new Object[] {linkId},
            new BeanPropertyRowMapper<>(TgChat.class)
        );
    }
}
