package edu.java.service.jdbc;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.TgChat;
import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.domain.repository.jdbc.JdbcLinksTrackingsRepository;
import edu.java.domain.repository.jdbc.JdbcTgChatsRepository;
import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatAlreadyExistsException;
import edu.java.service.exceptions.TgChatNotFoundException;
import edu.java.service.TgChatsService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcTgChatsService implements TgChatsService {
    private final JdbcTgChatsRepository tgChatsRepository;
    private final JdbcLinksRepository linksRepository;
    private final JdbcLinksTrackingsRepository linksTrackingsRepository;

    @Override
    public void register(long tgChatId) {
        try {
            tgChatsRepository.add(tgChatId);
        } catch (DataAccessException e) {
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
    public List<TgChat> listAll(URI linkUri) {
        Link link = linksRepository.findByUrl(linkUri)
            .orElseThrow(LinkNotFoundException::new);

        return linksTrackingsRepository.findAllTgChatsByLink(link.id());
    }
}
