package edu.java.service.jooq;

import edu.java.domain.jooq.tables.pojos.Links;
import edu.java.domain.model.jdbc.TgChat;
import edu.java.domain.repository.jooq.JooqLinksRepository;
import edu.java.domain.repository.jooq.JooqLinksTrackingsRepository;
import edu.java.domain.repository.jooq.JooqTgChatsRepository;
import edu.java.service.TgChatsService;
import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatAlreadyExistsException;
import edu.java.service.exceptions.TgChatNotFoundException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JooqTgChatsService implements TgChatsService {
    private final JooqTgChatsRepository tgChatsRepository;
    private final JooqLinksRepository linksRepository;
    private final JooqLinksTrackingsRepository linksTrackingsRepository;

    @Override
    public void register(long tgChatId) {
        if (!tgChatsRepository.add(tgChatId)) {
            throw new TgChatAlreadyExistsException();
        }
    }

    @Override
    public void unregister(long tgChatId) {
        if (!tgChatsRepository.remove(tgChatId)) {
            throw new TgChatNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TgChat> listAll(URI linkUrl) {
        Links link = linksRepository.findByUrl(linkUrl)
            .orElseThrow(LinkNotFoundException::new);

        return linksTrackingsRepository.findAllTgChatsByLink(link.getId()).stream()
            .map(chat -> new TgChat(chat.getId(), chat.getCreatedAt()))
            .toList();
    }
}
