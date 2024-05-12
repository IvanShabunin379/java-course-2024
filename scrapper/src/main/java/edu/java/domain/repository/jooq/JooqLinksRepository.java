package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.pojos.Links;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.Links.LINKS;

@Repository
@RequiredArgsConstructor
public class JooqLinksRepository {
    private final DSLContext dslContext;

    public boolean add(URI url) {
        try {
            dslContext.insertInto(LINKS)
                .set(LINKS.URL, url.toString())
                .execute();
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean removeById(long id) {
        return dslContext.deleteFrom(LINKS)
            .where(LINKS.ID.eq(id))
            .execute() == 1;
    }

    public boolean removeByUrl(URI url) {
        return dslContext.deleteFrom(LINKS)
            .where(LINKS.URL.eq(url.toString()))
            .execute() == 1;
    }

    public List<Links> findAll() {
        return dslContext.selectFrom(LINKS)
            .fetchInto(Links.class);
    }

    public List<Links> findUncheckedLinksForLongestTime(int limit) {
        return dslContext.selectFrom(LINKS)
            .limit(limit)
            .fetchInto(Links.class);
    }

    public Optional<Links> findById(long id) {
        return dslContext.selectFrom(LINKS)
            .where(LINKS.ID.eq(id))
            .fetchOptionalInto(Links.class);
    }

    public Optional<Links> findByUrl(URI url) {
        return dslContext.selectFrom(LINKS)
            .where(LINKS.URL.eq(url.toString()))
            .fetchOptionalInto(Links.class);
    }

    public boolean update(Links link) {
        return dslContext.update(LINKS)
            .set(LINKS.URL, link.getUrl())
            .set(LINKS.LAST_CHECK_TIME, link.getLastCheckTime())
            .where(LINKS.ID.eq(link.getId()))
            .execute() == 1;
    }
}
