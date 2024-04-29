package edu.java.service.jooq;

import edu.java.domain.jooq.tables.pojos.Links;
import edu.java.domain.jooq.tables.pojos.LinksTrackings;
import edu.java.domain.jooq.tables.pojos.TgChats;
import edu.java.domain.model.Link;
import edu.java.domain.repository.jooq.JooqLinksRepository;
import edu.java.domain.repository.jooq.JooqLinksTrackingsRepository;
import edu.java.domain.repository.jooq.JooqTgChatsRepository;
import edu.java.exceptions.LinkInChatAlreadyExistsException;
import edu.java.exceptions.LinkInChatNotFoundException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.exceptions.TgChatNotFoundException;
import edu.java.service.LinksService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class JooqLinksService implements LinksService {
    private final JooqLinksRepository linksRepository;
    private final JooqTgChatsRepository tgChatsRepository;
    private final JooqLinksTrackingsRepository linksTrackingsRepository;

    @Override
    public Link add(long tgChatId, URI url) {
        TgChats tgChat = tgChatsRepository.findById(tgChatId)
            .orElseThrow(TgChatNotFoundException::new);

        linksRepository.add(url);
        Links link = linksRepository.findByUrl(url).get();

        if (!linksTrackingsRepository.add(new LinksTrackings(tgChatId, link.getId()))) {
            throw new LinkInChatAlreadyExistsException();
        };

        return new Link(link.getId(), URI.create(link.getUrl()), link.getLastCheckTime());
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        TgChats chat = tgChatsRepository.findById(tgChatId)
            .orElseThrow(TgChatNotFoundException::new);
        Links link = linksRepository.findByUrl(url)
            .orElseThrow(LinkNotFoundException::new);

        if (!linksTrackingsRepository.remove(new LinksTrackings(tgChatId, link.getId()))) {
            throw new LinkInChatNotFoundException();
        }

        if (linksTrackingsRepository.findAllTgChatsByLink(link.getId()).isEmpty()) {
            linksRepository.removeById(link.getId());
        }

        return new Link(link.getId(), URI.create(link.getUrl()), link.getLastCheckTime());
    }

    @Override
    public void updateLastCheckTime(long id, OffsetDateTime lastCheckTime) {
        Links link = linksRepository.findById(id)
            .orElseThrow(LinkNotFoundException::new);

        linksRepository.update(new Links(link.getId(), link.getUrl(), lastCheckTime));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Link> listAll(long tgChatId) {
        TgChats chat = tgChatsRepository.findById(tgChatId)
            .orElseThrow(TgChatNotFoundException::new);

        return linksTrackingsRepository.findAllLinksByTgChat(tgChatId).stream()
            .map(link -> new Link(link.getId(), URI.create(link.getUrl()), link.getLastCheckTime()))
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Link> findUncheckedLinksForLongestTime(int limit) {
        return linksRepository.findUncheckedLinksForLongestTime(limit).stream()
            .map(link -> new Link(link.getId(), URI.create(link.getUrl()), link.getLastCheckTime()))
            .toList();
    }
}
