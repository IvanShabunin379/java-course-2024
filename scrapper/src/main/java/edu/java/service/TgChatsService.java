package edu.java.service;

import edu.java.domain.model.jdbc.TgChat;
import java.net.URI;
import java.util.List;

public interface TgChatsService {
    void register(long tgChatId);

    void unregister(long tgChatId);

    List<TgChat> listAll(URI linkUri);
}
