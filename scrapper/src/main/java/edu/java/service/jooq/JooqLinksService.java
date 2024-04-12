package edu.java.service.jooq;

import edu.java.domain.model.Link;
import edu.java.service.LinksService;
import java.net.URI;
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
