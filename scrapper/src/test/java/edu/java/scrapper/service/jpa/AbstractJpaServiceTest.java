package edu.java.scrapper.service.jpa;

import edu.java.IntegrationTest;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import edu.java.service.jpa.JpaLinksService;
import edu.java.service.jpa.JpaTgChatsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.net.URI;

@DataJpaTest
public class AbstractJpaServiceTest extends IntegrationTest {
    protected final static Long REGISTERED_TG_CHAT_ID = 1L;
    protected final static Long TEST_TG_CHAT_ID = 2L;
    protected final static URI TRACKED_TEST_GITHUB_LINK_URL = URI.create("https://github.com/luca_modric/java-course-2024");
    protected final static URI TEST_STACKOVERFLOW_LINK_URL = URI.create("https://stackoverflow.com/questions/123");
    protected final static URI ANOTHER_TEST_GITHUB_LINK_URL = URI.create("https://github.com/aaa/aaa");

    @Autowired
    protected JpaLinksRepository linksRepository;
    @Autowired
    protected JpaTgChatsRepository tgChatsRepository;
    protected JpaLinksService linksService;
    protected JpaTgChatsService tgChatsService;

    @BeforeEach
    public void setUp() {
        linksService = new JpaLinksService(linksRepository, tgChatsRepository);
        tgChatsService = new JpaTgChatsService(tgChatsRepository, linksRepository);

        tgChatsService.register(REGISTERED_TG_CHAT_ID);
        linksService.add(REGISTERED_TG_CHAT_ID, TRACKED_TEST_GITHUB_LINK_URL);
    }

    @AfterEach
    public void tearDown() {
        linksRepository.deleteAll();
        tgChatsRepository.deleteAll();
    }
}
