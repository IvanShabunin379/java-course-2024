package edu.java.service.jdbc;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.LinkTracking;
import edu.java.domain.model.jdbc.TgChat;
import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.domain.repository.jdbc.JdbcLinksTrackingsRepository;
import edu.java.domain.repository.jdbc.JdbcTgChatsRepository;
import edu.java.service.exceptions.LinkInChatAlreadyExistsException;
import edu.java.service.exceptions.LinkInChatNotFoundException;
import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatNotFoundException;
import edu.java.service.LinksService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcLinksService implements LinksService {
    private final JdbcLinksRepository linksRepository;
    private final JdbcTgChatsRepository tgChatsRepository;
    private final JdbcLinksTrackingsRepository linksTrackingsRepository;

    @Override
    public Link add(long tgChatId, URI url) {
        TgChat chat = tgChatsRepository.findById(tgChatId).orElseThrow(TgChatNotFoundException::new);

        linksRepository.add(url);
        Link link = linksRepository.findByUrl(url).get();

        if (!linksTrackingsRepository.add(new LinkTracking(tgChatId, link.getId()))) {
            throw new LinkInChatAlreadyExistsException();
        }

        return link;
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        TgChat chat = tgChatsRepository.findById(tgChatId).orElseThrow(TgChatNotFoundException::new);
        Link link = linksRepository.findByUrl(url).orElseThrow(LinkNotFoundException::new);

        if (!linksTrackingsRepository.remove(new LinkTracking(tgChatId, link.getId()))) {
            throw new LinkInChatNotFoundException();
        }

        if (linksTrackingsRepository.findAllTgChatsByLink(link.getId()).isEmpty()) {
            linksRepository.removeById(link.getId());
        }

        return link;
    }

    @Override
    public void updateLastCheckTime(long id, OffsetDateTime lastCheckTime) {
        Link link = linksRepository.findById(id)
            .orElseThrow(LinkNotFoundException::new);

        linksRepository.update(new Link(link.getId(), link.getUrl(), lastCheckTime));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Link> listAll(long tgChatId) {
        TgChat chat = tgChatsRepository.findById(tgChatId)
            .orElseThrow(TgChatNotFoundException::new);
        return linksTrackingsRepository.findAllLinksByTgChat(tgChatId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Link> findUncheckedLinksForLongestTime(int limit) {
        return linksRepository.findUncheckedLinksForLongestTime(limit);
    }
}
