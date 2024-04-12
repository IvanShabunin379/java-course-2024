package edu.java.service.jooq;

import edu.java.domain.model.TgChat;
import edu.java.service.TgChatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;

@Service
@Transactional
public class JooqTgChatsService implements TgChatsService {
    @Override
    public void register(long tgChatId) {

    }

    @Override
    public void unregister(long tgChatId) {

    }

    @Override
    public List<TgChat> listAll(URI linkUri) {
        return null;
    }
}
