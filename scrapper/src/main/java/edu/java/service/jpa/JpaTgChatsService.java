package edu.java.service.jpa;

import edu.java.domain.model.jdbc.TgChat;
import edu.java.domain.model.jpa.LinkEntity;
import edu.java.domain.model.jpa.TgChatEntity;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import edu.java.service.TgChatsService;
import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatAlreadyExistsException;
import edu.java.service.exceptions.TgChatNotFoundException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JpaTgChatsService implements TgChatsService {
    private final JpaTgChatsRepository tgChatsRepository;
    private final JpaLinksRepository linksRepository;

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

        if (unregistredChat.getLinks() != null) {
            for (LinkEntity trackedLink : unregistredChat.getLinks()) {
                trackedLink.getTgChats().remove(unregistredChat);
                linksRepository.save(trackedLink);

                if (trackedLink.getTgChats().isEmpty()) {
                    linksRepository.delete(trackedLink);
                }
            }
        }

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
