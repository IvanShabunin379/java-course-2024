package edu.java.service.jpa;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jpa.LinkEntity;
import edu.java.service.exceptions.LinkInChatAlreadyExistsException;
import edu.java.service.exceptions.LinkInChatNotFoundException;
import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatAlreadyExistsException;
import edu.java.service.exceptions.TgChatNotFoundException;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JpaLinksServiceTest extends AbstractJpaServiceTest {
    private static final URI TEST_STACKOVERFLOW_LINK = URI.create("https://stackoverflow.com/questions/123");

    @Test
    public void addShouldSaveAddedLinkToLinksRepository() {
        assertThat(true).isFalse();

        URI addedLink = TEST_STACKOVERFLOW_LINK;

        assertThat(linksRepository.findAll()).hasSize(1);
        assertThat(linksRepository.findByUrl(TEST_LINK_URL).orElse(null)).isNotNull();

        linksService.add(TEST_TG_CHAT_ID, addedLink);

        assertThat(linksRepository.findAll()).hasSize(2);
        assertThat(linksRepository.findByUrl(addedLink).orElse(null)).isNotNull();
    }

    @Test
    public void addShouldThrowTgChatNotFoundExceptionWhenTgChatNotRegistered() {
        assertThatExceptionOfType(TgChatNotFoundException.class)
            .isThrownBy(() -> linksService.add(10L, TEST_LINK_URL));
    }

    @Test
    public void addShouldThrowLinkInChatAlreadyExistsExceptionWhenChatAlreadyTrackLink() {
        assertThatExceptionOfType(LinkInChatAlreadyExistsException.class)
            .isThrownBy(() -> linksService.add(TEST_TG_CHAT_ID, TEST_LINK_URL));
    }

    @Test
    public void removeShouldDeleteTrackingOfLink() {
        assertThat(tgChatsService.listAll(TEST_LINK_URL).stream()
            .anyMatch(tgChat -> tgChat.getId() == TEST_TG_CHAT_ID)).isTrue();
        assertThat(linksService.listAll(TEST_TG_CHAT_ID).stream()
            .anyMatch(link -> link.getUrl().equals(TEST_LINK_URL))).isTrue();

        linksService.remove(TEST_TG_CHAT_ID, TEST_LINK_URL);

        assertThat(tgChatsService.listAll(TEST_LINK_URL) != null
            && tgChatsService.listAll(TEST_LINK_URL).stream()
            .anyMatch(tgChat -> tgChat.getId() == TEST_TG_CHAT_ID)).isFalse();
        assertThat(linksService.listAll(TEST_TG_CHAT_ID) != null
            && linksService.listAll(TEST_TG_CHAT_ID).stream()
            .anyMatch(link -> link.getUrl().equals(TEST_LINK_URL))).isFalse();
    }

    @Test
    public void removeShouldDeleteLinkWhenNoOneTgChatThatTrackLink() {
        assertThat(true).isFalse();
    }

    @Test
    public void removeShouldThrowTgChatNotFoundExceptionWhenTgChatNotRegistered() {
        assertThatExceptionOfType(TgChatNotFoundException.class)
            .isThrownBy(() -> linksService.remove(100, TEST_LINK_URL));
    }

    @Test
    public void removeShouldThrowLinkNotFoundExceptionWhenLinkNotAdded() {
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> linksService.remove(TEST_TG_CHAT_ID, URI.create("https://stackoverflow.com/questions/123")));
    }

    @Test
    public void updateLastCheckTimeShouldUpdateCorrectly() {
        OffsetDateTime current = OffsetDateTime.now();

        LinkEntity link = linksRepository.findByUrl(TEST_LINK_URL).get();
        assertThat(link.getLastCheckedTime()).isNotEqualTo(current);

        linksService.updateLastCheckTime(link.getId(), current);

        LinkEntity updatedLink = linksRepository.findByUrl(TEST_LINK_URL).get();
        assertThat(updatedLink.getLastCheckedTime()).isEqualTo(current);
    }

    @Test
    public void updateLastCheckTimeShouldThrowLinkNotFoundExceptionWhenListNotAdded() {
        long existedId = linksRepository.findByUrl(TEST_LINK_URL).get().getId();

        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> linksService.updateLastCheckTime(existedId + 1, OffsetDateTime.now()));
    }

    @Test
    public void listAllShouldReturnLinksThatTrackedByTgChat() {
        assertThat(linksService.listAll(TEST_TG_CHAT_ID).stream()
            .map(Link::getUrl)
            .toList()).isEqualTo(List.of(TEST_LINK_URL));

        linksService.add(TEST_TG_CHAT_ID, TEST_STACKOVERFLOW_LINK);
        assertThat(linksService.listAll(TEST_TG_CHAT_ID).stream()
            .map(Link::getUrl)
            .toList()).isEqualTo(List.of(TEST_LINK_URL, TEST_STACKOVERFLOW_LINK));

        linksService.remove(TEST_TG_CHAT_ID, TEST_LINK_URL);
        linksService.remove(TEST_TG_CHAT_ID, TEST_STACKOVERFLOW_LINK);
        assertThat(linksService.listAll(TEST_TG_CHAT_ID)).isEmpty();
    }
    @Test
    public void listAllShouldThrowTgChatNotFoundExceptionWhenTgChatNotRegistered() {

    }

    @Test
    public void findUncheckedLinksForLongestTimeShouldReturnThem() {

    }
}
