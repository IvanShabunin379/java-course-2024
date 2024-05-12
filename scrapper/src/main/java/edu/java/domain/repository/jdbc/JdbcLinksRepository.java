package edu.java.domain.repository.jdbc;

import edu.java.domain.model.jdbc.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinksRepository {
    private final JdbcTemplate jdbcTemplate;

    public boolean add(URI url) {
        String sql = """
            INSERT INTO links(url)
            VALUES (?)
            """;

        try {
            jdbcTemplate.update(sql, url.toString());
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean removeById(long id) {
        return jdbcTemplate.update("DELETE FROM links WHERE id = ?", id) == 1;
    }

    public boolean removeByUrl(URI url) {
        return jdbcTemplate.update("DELETE FROM links WHERE url = ?", url.toString()) == 1;
    }

    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM links", new BeanPropertyRowMapper<>(Link.class));
    }

    public List<Link> findUncheckedLinksForLongestTime(int limit) {
        return jdbcTemplate.query(
            "SELECT * FROM links ORDER BY last_check_time ASC LIMIT (?)",
            new Object[] {limit},
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
                new Object[] {url.toString()},
                new BeanPropertyRowMapper<>(Link.class)
            );
            return Optional.ofNullable(link);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean update(Link link) {
        return jdbcTemplate.update(
            "UPDATE links SET url = ?, last_check_time = ? WHERE id = ?",
            link.getUrl().toString(),
            link.getLastCheckTime(),
            link.getId()
        ) == 1;
    }
}
