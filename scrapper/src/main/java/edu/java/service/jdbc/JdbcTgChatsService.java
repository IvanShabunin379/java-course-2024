package edu.java.service.jdbc;

import edu.java.domain.model.Link;
import edu.java.domain.model.TgChat;
import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.domain.repository.jdbc.JdbcLinksTrackingsRepository;
import edu.java.domain.repository.jdbc.JdbcTgChatsRepository;
import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatAlreadyExistsException;
import edu.java.service.exceptions.TgChatNotFoundException;
import edu.java.service.TgChatsService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;

@Service
@Transactional
public class JdbcTgChatsService implements TgChatsService {
    private final JdbcTgChatsRepository tgChatsRepository;
    private final JdbcLinksRepository linksRepository;
    private final JdbcLinksTrackingsRepository linksTrackingsRepository;

    public JdbcTgChatsService(JdbcTgChatsRepository tgChatsRepository, JdbcLinksRepository linksRepository, JdbcLinksTrackingsRepository linksTrackingsRepository) {
        this.tgChatsRepository = tgChatsRepository;
        this.linksRepository = linksRepository;
        this.linksTrackingsRepository = linksTrackingsRepository;
    }

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

    @Override
    public List<TgChat> listAll(URI linkUri) {
        Link link = linksRepository.findByUrl(linkUri)
            .orElseThrow(LinkNotFoundException::new);

        return linksTrackingsRepository.findAllTgChatsByLink(link.id());
    }
}
