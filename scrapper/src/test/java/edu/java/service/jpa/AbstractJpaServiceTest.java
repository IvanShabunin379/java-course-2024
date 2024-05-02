package edu.java.service.jpa;

import edu.java.IntegrationTest;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import java.net.URI;

//@SpringBootTest
@DataJpaTest
public class AbstractJpaServiceTest extends IntegrationTest {
    protected final static Long TEST_TG_CHAT_ID = 1L;
    protected final static URI TEST_LINK_URL = URI.create("https://github.com/luca_modric/java-course-2024");

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

        tgChatsService.register(TEST_TG_CHAT_ID);
        linksService.add(TEST_TG_CHAT_ID, TEST_LINK_URL);
    }

    @AfterEach
    public void tearDown() {
        linksRepository.deleteAll();
        tgChatsRepository.deleteAll();
    }
}
