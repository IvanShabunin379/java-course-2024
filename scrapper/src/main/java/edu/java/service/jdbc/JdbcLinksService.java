package edu.java.service.jdbc;

import edu.java.domain.model.Link;
import edu.java.domain.model.LinkTracking;
import edu.java.domain.model.TgChat;
import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.domain.repository.jdbc.JdbcLinksTrackingsRepository;
import edu.java.domain.repository.jdbc.JdbcTgChatsRepository;
import edu.java.service.LinksService;
import edu.java.exceptions.LinkInChatAlreadyExistsException;
import edu.java.exceptions.LinkInChatNotFoundException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.exceptions.TgChatNotFoundException;
import java.net.URI;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JdbcLinksService implements LinksService {
    private final JdbcTgChatsRepository tgChatsRepository;
    private final JdbcLinksRepository linksRepository;
    private final JdbcLinksTrackingsRepository linksTrackingsRepository;

    public JdbcLinksService(
        JdbcTgChatsRepository tgChatsRepository,
        JdbcLinksRepository linksRepository,
        JdbcLinksTrackingsRepository linksTrackingsRepository
    ) {
        this.tgChatsRepository = tgChatsRepository;
        this.linksRepository = linksRepository;
        this.linksTrackingsRepository = linksTrackingsRepository;
    }

    @Transactional
    @Override
    public Link add(long tgChatId, URI url) {
        TgChat chat = tgChatsRepository.findById(tgChatId).orElseThrow(TgChatNotFoundException::new);

        linksRepository.add(url);
        Link link = linksRepository.findByUrl(url).get();

        try {
            linksTrackingsRepository.add(new LinkTracking(tgChatId, link.id()));
        } catch (DataAccessException e) {
            throw new LinkInChatAlreadyExistsException();
        }

        return link;
    }

    @Transactional
    @Override
    public Link remove(long tgChatId, URI url) {
        TgChat chat = tgChatsRepository.findById(tgChatId).orElseThrow(TgChatNotFoundException::new);
        Link link = linksRepository.findByUrl(url).orElseThrow(LinkNotFoundException::new);

        if (!linksTrackingsRepository.remove(new LinkTracking(tgChatId, link.id()))) {
            throw new LinkInChatNotFoundException();
        }

        if (linksTrackingsRepository.findAllTgChatsByLink(link.id()).isEmpty()) {
            linksRepository.removeById(link.id());
        }

        return link;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Link> listAll(long tgChatId) {
        TgChat chat = tgChatsRepository.findById(tgChatId)
            .orElseThrow(TgChatNotFoundException::new);
        return linksTrackingsRepository.findAllLinksByTgChat(tgChatId);
    }

    @Override
    public List<Link> findUncheckedLinksForLongestTime(int limit) {
        return linksRepository.findUncheckedLinksForLongestTime(limit);
    }
}
