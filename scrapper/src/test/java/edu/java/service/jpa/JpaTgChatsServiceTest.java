package edu.java.service.jpa;

import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatAlreadyExistsException;
import edu.java.service.exceptions.TgChatNotFoundException;
import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JpaTgChatsServiceTest extends AbstractJpaServiceTest {
    @Test
    public void registerShouldAddRegisteredChatToTgChatsRepository() {
        long addedTgChatId = 2;

        assertThat(tgChatsRepository.findAll()).hasSize(1);
        assertThat(tgChatsRepository.existsById(TEST_TG_CHAT_ID)).isTrue();
        assertThat(tgChatsRepository.findById(TEST_TG_CHAT_ID).get().getId()).isEqualTo(TEST_TG_CHAT_ID);

        tgChatsService.register(addedTgChatId);

        assertThat(tgChatsRepository.findAll()).hasSize(2);
        assertThat(tgChatsRepository.existsById(addedTgChatId)).isTrue();
        assertThat(tgChatsRepository.findById(addedTgChatId).get().getId()).isEqualTo(addedTgChatId);
    }

    @Test
    public void registerShouldThrowTgChatsAlreadyExistsExceptionWhenChatAlreadyRegistered() {
        assertThatExceptionOfType(TgChatAlreadyExistsException.class)
            .isThrownBy(() -> tgChatsService.register(TEST_TG_CHAT_ID));
    }

    @Test
    public void unregisterShouldDeleteChatFromTgChatsRepository() {
        assertThat(tgChatsRepository.findAll()).hasSize(1);
        assertThat(tgChatsRepository.existsById(TEST_TG_CHAT_ID)).isTrue();
        assertThat(tgChatsRepository.findById(TEST_TG_CHAT_ID).get().getId()).isEqualTo(TEST_TG_CHAT_ID);

        tgChatsService.unregister(TEST_TG_CHAT_ID);

        assertThat(tgChatsRepository.findAll()).hasSize(0);
        assertThat(tgChatsRepository.existsById(TEST_TG_CHAT_ID)).isFalse();
        assertThat(tgChatsRepository.findById(TEST_TG_CHAT_ID).orElse(null)).isNull();
    }

    @Test
    public void unregisterShouldThrowTgChatsNotFoundExceptionWhenChatNotRegistered() {
        assertThat(tgChatsRepository.existsById(2L)).isFalse();
        assertThatExceptionOfType(TgChatNotFoundException.class)
            .isThrownBy(() -> tgChatsService.unregister(2L));
    }

    @Test
    public void listAllShouldReturnTgChatsThatTrackLink() {
        //assertThat(tgChatsService.listAll(URI.create("https://github.com/aaa/aaa"))).isEmpty();
        assertThat(tgChatsService.listAll(TEST_LINK_URL)).hasSize(1);
        linksService.add(TEST_TG_CHAT_ID, URI.create("https://github.com/aaa/aaa"));
        assertThat(tgChatsService.listAll(URI.create("https://github.com/aaa/aaa"))).hasSize(1);
    }

    @Test
    public void listAllShouldThrowLinkNotFoundExceptionWhenListNotAdded() {
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> tgChatsService.listAll(URI.create("https://github.com/aaa/aaa")));
    }
}
