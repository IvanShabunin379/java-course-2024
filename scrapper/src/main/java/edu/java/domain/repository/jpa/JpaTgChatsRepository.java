package edu.java.domain.repository.jpa;

import edu.java.domain.model.jpa.TgChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTgChatsRepository extends JpaRepository<TgChatEntity, Long> {
}
