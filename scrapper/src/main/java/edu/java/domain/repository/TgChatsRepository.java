package edu.java.domain.repository;

import edu.java.domain.model.TgChat;
import java.util.List;
import java.util.Optional;

public interface TgChatsRepository {
    void add(long id);
    boolean remove(long id);
    List<TgChat> findAll();
    Optional<TgChat> findById(long id);
}
