package edu.java.service.jooq;

import edu.java.domain.model.jdbc.Link;
import edu.java.service.LinksService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JooqLinksService implements LinksService {
    @Override
    public Link add(long tgChatId, URI url) {
        return null;
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        return null;
    }

    @Override
    public void updateLastCheckTime(long id, OffsetDateTime lastCheckTime) {

    }

    @Transactional(readOnly = true)
    @Override
    public List<Link> listAll(long tgChatId) {
        return null;
    }

    @Override
    public List<Link> findUncheckedLinksForLongestTime(int limit) {
        return null;
    }
}
