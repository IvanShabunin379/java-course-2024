package edu.java.service.jpa;

import edu.java.domain.model.jdbc.Link;
import edu.java.service.LinksService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.List;

@Service
@Transactional
public class JpaLinksService implements LinksService {
    @Override
    public Link add(long tgChatId, URI url) {
        return null;
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        return null;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return null;
    }
}
