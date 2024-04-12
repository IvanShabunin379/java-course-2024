package edu.java.service.jpa;

import edu.java.domain.model.jpa.TgChatEntity;
import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import edu.java.exceptions.TgChatAlreadyExistsException;
import edu.java.exceptions.TgChatNotFoundException;
import edu.java.service.TgChatsService;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;

@Transactional
public class JpaTgChatsService implements TgChatsService {
    private final JpaTgChatsRepository tgChatsRepository;

    public JpaTgChatsService(JpaTgChatsRepository tgChatsRepository) {
        this.tgChatsRepository = tgChatsRepository;
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
}
