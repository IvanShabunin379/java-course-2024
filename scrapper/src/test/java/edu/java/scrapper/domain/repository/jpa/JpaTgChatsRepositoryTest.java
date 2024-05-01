package edu.java.scrapper.domain.repository.jpa;

import edu.java.domain.model.jdbc.TgChat;
import edu.java.domain.model.jpa.TgChatEntity;
import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor
public class JpaTgChatsRepositoryTest {
    private static final OffsetDateTime TEST_CREATED_TIMESTAMP = OffsetDateTime.of (2024, 4, 30, 14, 0, 0, 0, ZoneOffset.UTC);

    private final JpaTgChatsRepository tgChatsRepository;
    private TgChatEntity testTgChat;

    @BeforeEach
    public void setUp() {
        testTgChat.setId(123);
        testTgChat.setCreatedAt(TEST_CREATED_TIMESTAMP);
        tgChatsRepository.save(testTgChat);
    }

    @AfterEach
    public void tearDown() {
        tgChatsRepository.delete(testTgChat);
    }

    @Test
    public void tgChatShouldCanBeFoundByIdWhenItCreated() {
        TgChatEntity tgChat = tgChatsRepository.findById(testTgChat.getId()).orElse(null);

        assertThat(tgChat).isNotNull();
        assertThat(tgChat.getId()).isEqualTo(testTgChat.getId());
        assertThat(tgChat.getCreatedAt()).isEqualTo(testTgChat.getCreatedAt());
    }

    @Test
    public void tgChatShouldCanNotBeFoundByIdWhenItCreated() {
        tgChatsRepository.delete(testTgChat);
        TgChatEntity tgChat = tgChatsRepository.findById(testTgChat.getId()).orElse(null);
        assertThat(tgChat).isNull();
    }
}
