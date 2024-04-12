package edu.java.service.jooq;

import edu.java.domain.model.jdbc.TgChat;
import edu.java.service.TgChatsService;
import java.net.URI;
import java.util.List;
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

    @Override
    public List<TgChat> listAll(URI linkUri) {
        return null;
    }
}
