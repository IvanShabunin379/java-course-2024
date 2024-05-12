package edu.java.scrapper.service.jpa;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.TgChat;
import edu.java.domain.model.jpa.LinkEntity;
import edu.java.service.exceptions.LinkInChatAlreadyExistsException;
import edu.java.service.exceptions.LinkNotFoundException;
import edu.java.service.exceptions.TgChatNotFoundException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JpaLinksServiceTest extends AbstractJpaServiceTest {
    @Test
    public void addShouldSaveLinkToChatTracking() {
        assertThat(linksService.listAll(REGISTERED_TG_CHAT_ID).stream()
            .map(Link::getUrl)
            .toList()).contains(TRACKED_TEST_GITHUB_LINK_URL);

        assertThat(tgChatsService.listAll(TRACKED_TEST_GITHUB_LINK_URL).stream()
            .map(TgChat::getId)
            .toList()).contains(REGISTERED_TG_CHAT_ID);
    }

    @Test
    public void addShouldThrowTgChatNotFoundExceptionWhenTgChatNotRegistered() {
        assertThatExceptionOfType(TgChatNotFoundException.class)
            .isThrownBy(() -> linksService.add(10L, TRACKED_TEST_GITHUB_LINK_URL));
    }

    @Test
    public void addShouldThrowLinkInChatAlreadyExistsExceptionWhenChatAlreadyTrackLink() {
        assertThatExceptionOfType(LinkInChatAlreadyExistsException.class)
            .isThrownBy(() -> linksService.add(REGISTERED_TG_CHAT_ID, TRACKED_TEST_GITHUB_LINK_URL));
    }

    @Test
    public void removeShouldDeleteTrackingOfLink() {
        tgChatsService.register(TEST_TG_CHAT_ID);
        linksService.add(TEST_TG_CHAT_ID, TRACKED_TEST_GITHUB_LINK_URL);

        assertThat(tgChatsService.listAll(TRACKED_TEST_GITHUB_LINK_URL).stream()
            .anyMatch(tgChat -> tgChat.getId() == REGISTERED_TG_CHAT_ID)).isTrue();
        assertThat(linksService.listAll(REGISTERED_TG_CHAT_ID).stream()
            .anyMatch(link -> link.getUrl().equals(TRACKED_TEST_GITHUB_LINK_URL))).isTrue();

        linksService.remove(REGISTERED_TG_CHAT_ID, TRACKED_TEST_GITHUB_LINK_URL);

        assertThat(tgChatsService.listAll(TRACKED_TEST_GITHUB_LINK_URL) != null
            && tgChatsService.listAll(TRACKED_TEST_GITHUB_LINK_URL).stream()
            .anyMatch(tgChat -> tgChat.getId() == REGISTERED_TG_CHAT_ID)).isFalse();
        assertThat(linksService.listAll(REGISTERED_TG_CHAT_ID) != null
            && linksService.listAll(REGISTERED_TG_CHAT_ID).stream()
            .anyMatch(link -> link.getUrl().equals(TRACKED_TEST_GITHUB_LINK_URL))).isFalse();
    }

    @Test
    public void removeShouldDeleteLinkWhenNoOneTgChatThatTrackLink() {
        assertThat(linksRepository.findByUrl(TRACKED_TEST_GITHUB_LINK_URL).orElse(null)).isNotNull();
        assertThat(tgChatsService.listAll(TRACKED_TEST_GITHUB_LINK_URL)).hasSize(1);

        linksService.remove(REGISTERED_TG_CHAT_ID, TRACKED_TEST_GITHUB_LINK_URL);

        assertThat(linksRepository.findByUrl(TRACKED_TEST_GITHUB_LINK_URL).orElse(null)).isNull();
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> tgChatsService.listAll(TRACKED_TEST_GITHUB_LINK_URL));
    }

    @Test
    public void removeShouldThrowTgChatNotFoundExceptionWhenTgChatNotRegistered() {
        assertThatExceptionOfType(TgChatNotFoundException.class)
            .isThrownBy(() -> linksService.remove(100, TRACKED_TEST_GITHUB_LINK_URL));
    }

    @Test
    public void removeShouldThrowLinkNotFoundExceptionWhenLinkNotAdded() {
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> linksService.remove(
                REGISTERED_TG_CHAT_ID,
                URI.create("https://stackoverflow.com/questions/123")
            ));
    }

    @Test
    public void updateLastCheckTimeShouldUpdateCorrectly() {
        OffsetDateTime current = OffsetDateTime.now();

        LinkEntity link = linksRepository.findByUrl(TRACKED_TEST_GITHUB_LINK_URL).get();
        assertThat(link.getLastCheckedTime()).isNotEqualTo(current);

        linksService.updateLastCheckTime(link.getId(), current);

        LinkEntity updatedLink = linksRepository.findByUrl(TRACKED_TEST_GITHUB_LINK_URL).get();
        assertThat(updatedLink.getLastCheckedTime()).isEqualTo(current);
    }

    @Test
    public void updateLastCheckTimeShouldThrowLinkNotFoundExceptionWhenListNotAdded() {
        long existedId = linksRepository.findByUrl(TRACKED_TEST_GITHUB_LINK_URL).get().getId();

        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> linksService.updateLastCheckTime(existedId + 1, OffsetDateTime.now()));
    }

    @Test
    public void listAllShouldReturnLinksThatTrackedByTgChat() {
        assertThat(linksService.listAll(REGISTERED_TG_CHAT_ID).stream()
            .map(Link::getUrl)
            .toList()).isEqualTo(List.of(TRACKED_TEST_GITHUB_LINK_URL));

        linksService.add(REGISTERED_TG_CHAT_ID, TEST_STACKOVERFLOW_LINK_URL);
        assertThat(linksService.listAll(REGISTERED_TG_CHAT_ID).stream()
            .map(Link::getUrl)
            .toList()).isEqualTo(List.of(TRACKED_TEST_GITHUB_LINK_URL, TEST_STACKOVERFLOW_LINK_URL));

        linksService.remove(REGISTERED_TG_CHAT_ID, TRACKED_TEST_GITHUB_LINK_URL);
        linksService.remove(REGISTERED_TG_CHAT_ID, TEST_STACKOVERFLOW_LINK_URL);
        assertThat(linksService.listAll(REGISTERED_TG_CHAT_ID)).isEmpty();
    }

    @Test
    public void listAllShouldThrowTgChatNotFoundExceptionWhenTgChatNotRegistered() {
        assertThatExceptionOfType(TgChatNotFoundException.class)
            .isThrownBy(() -> linksService.listAll(TEST_TG_CHAT_ID));
    }

    @Test
    public void findUncheckedLinksForLongestTimeShouldReturnCorrectLinks() {
        int limit = 10;

        assertThat(linksService.findUncheckedLinksForLongestTime(limit).stream()
            .map(Link::getUrl)
            .toList()).isEqualTo(List.of(TRACKED_TEST_GITHUB_LINK_URL));

        linksService.add(REGISTERED_TG_CHAT_ID, TEST_STACKOVERFLOW_LINK_URL);
        linksService.add(REGISTERED_TG_CHAT_ID, ANOTHER_TEST_GITHUB_LINK_URL);
        assertThat(linksService.findUncheckedLinksForLongestTime(limit).stream()
            .map(Link::getUrl)
            .toList()).isEqualTo(List.of(
            TRACKED_TEST_GITHUB_LINK_URL,
            TEST_STACKOVERFLOW_LINK_URL,
            ANOTHER_TEST_GITHUB_LINK_URL
        ));

        linksService.remove(REGISTERED_TG_CHAT_ID, TRACKED_TEST_GITHUB_LINK_URL);
        linksService.remove(REGISTERED_TG_CHAT_ID, TEST_STACKOVERFLOW_LINK_URL);
        linksService.remove(REGISTERED_TG_CHAT_ID, ANOTHER_TEST_GITHUB_LINK_URL);
        assertThat(linksService.findUncheckedLinksForLongestTime(limit)).isEmpty();
    }
}
