package edu.java.service.jdbc;

import edu.java.domain.repository.jdbc.JdbcTgChatsRepository;
import edu.java.exceptions.TgChatAlreadyExistsException;
import edu.java.exceptions.TgChatNotFoundException;
import edu.java.service.TgChatsService;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcTgChatsService implements TgChatsService {
    private final JdbcTgChatsRepository tgChatsRepository;

    public JdbcTgChatsService(JdbcTgChatsRepository tgChatsRepository) {
        this.tgChatsRepository = tgChatsRepository;
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
}
