package edu.java.domain.repository.jpa;

import edu.java.IntegrationTest;
import edu.java.domain.model.jpa.LinkEntity;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Limit;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaLinksRepositoryTest extends IntegrationTest {
    private static final OffsetDateTime
        TEST_LAST_CHECKED_TIMESTAMP = OffsetDateTime.of(2024, 4, 30, 14, 0, 0, 0, ZoneOffset.UTC);
    @Autowired
    private JpaLinksRepository linksRepository;

    private LinkEntity testLink;

    @BeforeEach
    public void setUp() {
        testLink = new LinkEntity();
        testLink.setId(1);
        testLink.setUrl(URI.create("https://github.com/IvanShabunin379/java-course-2024"));
        testLink.setLastCheckedTime(TEST_LAST_CHECKED_TIMESTAMP);

        linksRepository.save(testLink);
    }

    @AfterEach
    public void tearDown() {
        linksRepository.deleteAll();
    }

    @Test
    public void linkShouldCanBeExistsAndFoundByIdWhenItCreated() {
        assertThat(linksRepository.existsById(testLink.getId())).isTrue();

        LinkEntity link = linksRepository.findById(testLink.getId()).orElse(null);

        assertThat(link).isNotNull();
        assertThat(link.getId()).isEqualTo(testLink.getId());
        assertThat(link.getUrl()).isEqualTo(testLink.getUrl());
        assertThat(link.getLastCheckedTime()).isEqualTo(testLink.getLastCheckedTime());
    }

    @Test
    public void linkShouldCanBeFoundByUrlWhenItCreated() {
        LinkEntity link = linksRepository.findByUrl(testLink.getUrl()).orElse(null);

        assertThat(link).isNotNull();
        assertThat(link.getId()).isEqualTo(testLink.getId());
        assertThat(link.getUrl()).isEqualTo(testLink.getUrl());
        assertThat(link.getLastCheckedTime()).isEqualTo(testLink.getLastCheckedTime());
    }

    @Test
    public void linkShouldCanBeExistsAndFoundByIdWithUpdatedDataWhenItUpdated() {
        OffsetDateTime current = OffsetDateTime.now();
        testLink.setLastCheckedTime(current);
        linksRepository.save(testLink);

        LinkEntity link = linksRepository.findById(testLink.getId()).orElse(null);

        assertThat(link).isNotNull();
        assertThat(link.getLastCheckedTime()).isEqualTo(current);
    }

    @Test
    public void linkShouldCanBeExistsAndFoundByUrlWithUpdatedDataWhenItUpdated() {
        OffsetDateTime current = OffsetDateTime.now();
        testLink.setLastCheckedTime(current);
        linksRepository.save(testLink);

        LinkEntity link = linksRepository.findByUrl(testLink.getUrl()).orElse(null);

        assertThat(link).isNotNull();
        assertThat(link.getLastCheckedTime()).isEqualTo(current);
    }

    @Test
    public void linkShouldCanNotBeFoundByIdWhenItDeleted() {
        assertThat(linksRepository.existsById(testLink.getId())).isTrue();

        linksRepository.delete(testLink);

        assertThat(linksRepository.existsById(testLink.getId())).isFalse();
        LinkEntity link = linksRepository.findById(testLink.getId()).orElse(null);
        assertThat(link).isNull();
    }

    @Test
    public void linkShouldCanNotBeFoundByUrlWhenItDeleted() {
        linksRepository.delete(testLink);

        LinkEntity link = linksRepository.findByUrl(testLink.getUrl()).orElse(null);
        assertThat(link).isNull();
    }

    @Test
    public void findAllByOrderByLastCheckedTimeShouldReturnSpecifiedNumberOfUncheckedLinksForLongestTime() {
        LinkEntity link1 = new LinkEntity();
        LinkEntity link2 = new LinkEntity();
        LinkEntity link3 = new LinkEntity();

        link1.setId(2);
        link2.setId(3);
        link3.setId(4);
        link1.setUrl(URI.create("https://github.com/luca_modric/java-course-2024"));
        link2.setUrl(URI.create("https://github.com/zinedin_zidane/java-course-2024"));
        link3.setUrl(URI.create("https://stackoverflow.com/questions/123"));
        link1.setLastCheckedTime(testLink.getLastCheckedTime().plusMinutes(5));
        link2.setLastCheckedTime(link1.getLastCheckedTime().plusMinutes(5));
        link3.setLastCheckedTime(link2.getLastCheckedTime().plusMinutes(5));

        linksRepository.save(link1);
        linksRepository.save(link2);
        linksRepository.save(link3);

        List<LinkEntity> result = linksRepository.findAllByOrderByLastCheckedTime(Limit.of(3));
        List<LinkEntity> expected = List.of(testLink, link1, link2);
        assertThat(result).isEqualTo(expected);
    }
}
