package edu.java.service.jpa;

import edu.java.domain.model.jdbc.TgChat;
import edu.java.domain.model.jpa.LinkEntity;
import edu.java.domain.model.jpa.TgChatEntity;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.exceptions.TgChatAlreadyExistsException;
import edu.java.exceptions.TgChatNotFoundException;
import edu.java.service.TgChatsService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JpaTgChatsService implements TgChatsService {
    private final JpaTgChatsRepository tgChatsRepository;
    private final JpaLinksRepository linksRepository;

    public JpaTgChatsService(JpaTgChatsRepository tgChatsRepository, JpaLinksRepository linksRepository) {
        this.tgChatsRepository = tgChatsRepository;
        this.linksRepository = linksRepository;
    }

    @Override
    public void register(long tgChatId) {
        if (tgChatsRepository.existsById(tgChatId)) {
            throw new TgChatAlreadyExistsException();
        }

        TgChatEntity newTgChat = new TgChatEntity();
        newTgChat.setId(tgChatId);
        newTgChat.setCreatedAt(OffsetDateTime.now());

        tgChatsRepository.save(newTgChat);
    }

    @Override
    public void unregister(long tgChatId) {
        TgChatEntity unregistredChat = tgChatsRepository.findById(tgChatId)
            .orElseThrow(TgChatNotFoundException::new);

        tgChatsRepository.delete(unregistredChat);
    }

    @Override
    public List<TgChat> listAll(URI linkUri) {
        LinkEntity link = linksRepository.findByUrl(linkUri)
            .orElseThrow(LinkNotFoundException::new);

        return link.getTgChats()
            .stream()
            .map(tgChatEntity -> new TgChat(tgChatEntity.getId(), tgChatEntity.getCreatedAt()))
            .toList();
    }
}
