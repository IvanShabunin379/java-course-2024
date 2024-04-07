package edu.java.domain.repository.jdbc;

import edu.java.domain.model.Link;
import edu.java.domain.repository.LinksRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcLinksRepository implements LinksRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLinksRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(URI url) {
        String sql = """
            INSERT INTO links(url)
            VALUES (?)
            """;
        jdbcTemplate.update(sql, url);
    }

    public boolean removeById(long id) {
        return jdbcTemplate.update("DELETE FROM links WHERE id = ?", id) == 1;
    }

    public boolean removeByUrl(URI url) {
        return jdbcTemplate.update("DELETE FROM links WHERE url = ?", url) == 1;
    }

    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM links", new BeanPropertyRowMapper<>(Link.class));
    }

    @Override
    public List<Link> findUncheckedLinksForLongestTime(int count) {
        return jdbcTemplate.query(
            "SELECT * FROM links ORDER BY last_checked_time ASC LIMIT ?",
            new Object[] {count},
            new BeanPropertyRowMapper<>(Link.class)
        );
    }

    public Optional<Link> findById(long id) {
        try {
            Link link = jdbcTemplate.queryForObject(
                "SELECT * FROM links WHERE id = ?",
                new Object[] {id},
                new BeanPropertyRowMapper<>(Link.class)
            );
            return Optional.ofNullable(link);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Link> findByUrl(URI url) {
        try {
            Link link = jdbcTemplate.queryForObject(
                "SELECT * FROM links WHERE url = ?",
                new Object[] {url},
                new BeanPropertyRowMapper<>(Link.class)
            );
            return Optional.ofNullable(link);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Link link) {
        return jdbcTemplate.update(
            "UPDATE links SET url = ?, last_checked_time = ? WHERE id = ?",
            link.url(),
            link.lastCheckTime(),
            link.id()
        ) == 1;
    }
}
