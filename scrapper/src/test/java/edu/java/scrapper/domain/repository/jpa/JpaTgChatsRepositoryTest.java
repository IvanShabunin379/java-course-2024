package edu.java.scrapper.domain.repository.jpa;

import edu.java.domain.model.jpa.TgChatEntity;
import edu.java.IntegrationTest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaTgChatsRepositoryTest extends IntegrationTest {
    private static final OffsetDateTime TEST_CREATED_TIMESTAMP =
        OffsetDateTime.of(2024, 4, 30, 14, 0, 0, 0, ZoneOffset.UTC);

    @Autowired
    private JpaTgChatsRepository tgChatsRepository;

    private TgChatEntity testTgChat;

    @BeforeEach
    public void setUp() {
        testTgChat = new TgChatEntity();
        testTgChat.setId(123);
        testTgChat.setCreatedAt(TEST_CREATED_TIMESTAMP);

        tgChatsRepository.save(testTgChat);
    }

    @AfterEach
    public void tearDown() {
        tgChatsRepository.deleteAll();
    }

    @Test
    public void tgChatShouldCanBeExistsAndFoundByIdWhenItCreated() {
        assertThat(tgChatsRepository.existsById(testTgChat.getId())).isTrue();

        TgChatEntity tgChat = tgChatsRepository.findById(testTgChat.getId()).orElse(null);

        assertThat(tgChat).isNotNull();
        assertThat(tgChat.getId()).isEqualTo(testTgChat.getId());
        assertThat(tgChat.getCreatedAt()).isEqualTo(testTgChat.getCreatedAt());
    }

    @Test
    public void tgChatShouldCanNotBeFoundByIdWhenItDeleted() {
        assertThat(tgChatsRepository.existsById(testTgChat.getId())).isTrue();

        tgChatsRepository.delete(testTgChat);

        assertThat(tgChatsRepository.existsById(testTgChat.getId())).isFalse();
        TgChatEntity tgChat = tgChatsRepository.findById(testTgChat.getId()).orElse(null);
        assertThat(tgChat).isNull();
    }
}
