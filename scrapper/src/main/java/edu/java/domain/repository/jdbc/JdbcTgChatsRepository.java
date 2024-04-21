package edu.java.domain.repository.jdbc;

import edu.java.domain.model.jdbc.TgChat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatsRepository {
    private final JdbcTemplate jdbcTemplate;

    public void add(long id) {
        String sql = """
            INSERT INTO tg_chats(id)
            VALUES (?)
            """;
        jdbcTemplate.update(sql, id);
    }

    public boolean remove(long id) {
        return jdbcTemplate.update("DELETE FROM tg_chats WHERE id = ?", id) == 1;
    }

    public List<TgChat> findAll() {
        return jdbcTemplate.query("SELECT * FROM tg_chats", new BeanPropertyRowMapper<>(TgChat.class));
    }

    public Optional<TgChat> findById(long id) {
        try {
            TgChat chat = jdbcTemplate.queryForObject(
                "SELECT * FROM tg_chats WHERE id = ?",
                new Object[] {id},
                new BeanPropertyRowMapper<>(TgChat.class)
            );

            return Optional.ofNullable(chat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
