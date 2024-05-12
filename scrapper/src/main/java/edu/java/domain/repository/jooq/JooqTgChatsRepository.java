package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.pojos.TgChats;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.TgChats.TG_CHATS;

@Repository
@RequiredArgsConstructor
public class JooqTgChatsRepository {
    private final DSLContext dslContext;

    public boolean add(long id) {
        try {
            dslContext.insertInto(TG_CHATS)
                .set(TG_CHATS.ID, id)
                .execute();
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public boolean remove(long id) {
        return dslContext.deleteFrom(TG_CHATS)
            .where(TG_CHATS.ID.eq(id))
            .execute() == 1;
    }

    public List<TgChats> findAll() {
        return dslContext.selectFrom(TG_CHATS)
            .fetchInto(TgChats.class);
    }

    public Optional<TgChats> findById(long id) {
        return dslContext.selectFrom(TG_CHATS)
            .where(TG_CHATS.ID.eq(id))
            .fetchOptionalInto(TgChats.class);
    }
}
