package edu.java.service.jpa;

import edu.java.domain.model.jdbc.TgChat;
import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatAlreadyExistsException;
import edu.java.service.exceptions.TgChatNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JpaTgChatsServiceTest extends AbstractJpaServiceTest {
    @Test
    public void registerShouldAddRegisteredChatToTgChatsRepository() {
        assertThat(tgChatsRepository.findAll()).hasSize(1);
        assertThat(tgChatsRepository.existsById(REGISTERED_TG_CHAT_ID)).isTrue();
        //assertThat(tgChatsRepository.findById(REGISTERED_TG_CHAT_ID).orElse(null)).isNotNull();

        tgChatsService.register(TEST_TG_CHAT_ID);

        assertThat(tgChatsRepository.findAll()).hasSize(2);
        assertThat(tgChatsRepository.existsById(TEST_TG_CHAT_ID)).isTrue();
        assertThat(tgChatsRepository.findById(TEST_TG_CHAT_ID).orElse(null)).isNotNull();
    }

    @Test
    public void registerShouldThrowTgChatsAlreadyExistsExceptionWhenChatAlreadyRegistered() {
        assertThatExceptionOfType(TgChatAlreadyExistsException.class)
            .isThrownBy(() -> tgChatsService.register(REGISTERED_TG_CHAT_ID));
    }

    @Test
    public void unregisterShouldDeleteChatFromTgChatsRepository() {
        assertThat(tgChatsRepository.findAll()).hasSize(1);
        assertThat(tgChatsRepository.existsById(REGISTERED_TG_CHAT_ID)).isTrue();
        assertThat(tgChatsRepository.findById(REGISTERED_TG_CHAT_ID).get().getId()).isEqualTo(
            REGISTERED_TG_CHAT_ID);

        tgChatsService.unregister(REGISTERED_TG_CHAT_ID);

        assertThat(tgChatsRepository.findAll()).hasSize(0);
        assertThat(tgChatsRepository.existsById(REGISTERED_TG_CHAT_ID)).isFalse();
        assertThat(tgChatsRepository.findById(REGISTERED_TG_CHAT_ID).orElse(null)).isNull();
    }

    @Test
    public void unregisterShouldThrowTgChatsNotFoundExceptionWhenChatNotRegistered() {
        assertThat(tgChatsRepository.existsById(2L)).isFalse();
        assertThatExceptionOfType(TgChatNotFoundException.class)
            .isThrownBy(() -> tgChatsService.unregister(2L));
    }

    @Test
    public void listAllShouldReturnTgChatsThatTrackLink() {
        assertThat(tgChatsService.listAll(TRACKED_TEST_GITHUB_LINK_URL).stream()
            .map(TgChat::getId)
            .toList()).isEqualTo(List.of(REGISTERED_TG_CHAT_ID));

        tgChatsService.register(TEST_TG_CHAT_ID);
        linksService.add(TEST_TG_CHAT_ID, TRACKED_TEST_GITHUB_LINK_URL);
        assertThat(tgChatsService.listAll(TRACKED_TEST_GITHUB_LINK_URL).stream()
            .map(TgChat::getId)
            .toList()).isEqualTo(List.of(REGISTERED_TG_CHAT_ID, TEST_TG_CHAT_ID));

        tgChatsService.unregister(REGISTERED_TG_CHAT_ID);
        tgChatsService.unregister(TEST_TG_CHAT_ID);
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> tgChatsService.listAll(TRACKED_TEST_GITHUB_LINK_URL));
    }

    @Test
    public void listAllShouldThrowLinkNotFoundExceptionWhenListNotAdded() {
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> tgChatsService.listAll(ANOTHER_TEST_GITHUB_LINK_URL));
    }
}
