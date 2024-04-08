package edu.java.domain.repository.jdbc;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.LinkTracking;
import edu.java.domain.model.jdbc.TgChat;
import edu.java.domain.repository.LinksTrackingsRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcLinksTrackingsRepository implements LinksTrackingsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLinksTrackingsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(LinkTracking linkTracking) {
        String sql = """
                INSERT INTO links_trackings(tg_chat_id, link_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(sql, linkTracking.tgChatId(), linkTracking.linkId());
    }

    public boolean remove(LinkTracking linkTracking) {
        String sql = """
                DELETE FROM links_trackings WHERE tg_chat_id = ? AND link_id = ?
                """;
        return jdbcTemplate.update(sql, linkTracking.tgChatId(), linkTracking.linkId()) == 1;
    }

    public List<Link> findAllLinksByTgChat(long tgChatId) {
        String sql = """
                SELECT L.*
                FROM links_trackings LT JOIN links L
                    ON LT.link_id = L.id
                WHERE LT.chat_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                new Object[]{tgChatId},
                new BeanPropertyRowMapper<>(Link.class)
        );
    }

    public List<TgChat> findAllTgChatsByLink(long linkId) {
        String sql = """
                SELECT T.*
                FROM links_trackings LT JOIN tg_chats T
                    ON LT.chat_id = T.id
                WHERE T.link_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                new Object[]{linkId},
                new BeanPropertyRowMapper<>(TgChat.class)
        );
    }
}
