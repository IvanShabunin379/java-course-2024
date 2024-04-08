package edu.java.service.jooq;

import edu.java.service.TgChatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JooqTgChatsService implements TgChatsService {
    @Override
    public void register(long tgChatId) {

    }

    @Override
    public void unregister(long tgChatId) {

    }
}
