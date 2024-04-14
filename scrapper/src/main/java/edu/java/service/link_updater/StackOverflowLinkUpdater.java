package edu.java.service.link_updater;

import edu.java.client.BotClient;
import edu.java.client.StackOverflowClient;
import edu.java.domain.model.Link;
import edu.java.domain.model.TgChat;
import edu.java.dto.LinkUpdateRequest;
import edu.java.responses.StackOverflowResponse;
import edu.java.responses.StackOverflowResponse.StackOverflowAnswerInfo;
import edu.java.service.LinksService;
import edu.java.service.TgChatsService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowLinkUpdater implements LinkUpdater<StackOverflowAnswerInfo> {
    private static final Pattern STACK_OVERFLOW_QUESTION_URL_PATTERN =
        Pattern.compile("https://stackoverflow.com/questions/(\\d+)");

    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;
    private final LinksService linksService;
    private final TgChatsService tgChatsService;

    public StackOverflowLinkUpdater(
        StackOverflowClient stackOverflowClient,
        BotClient botClient,
        LinksService linksService,
        TgChatsService tgChatsService
    ) {
        this.stackOverflowClient = stackOverflowClient;
        this.botClient = botClient;
        this.linksService = linksService;
        this.tgChatsService = tgChatsService;
    }

    @Override
    public List<StackOverflowAnswerInfo> getUpdatesForLink(Link link) {
        long questionId = parseQuestionId(link.url());

        OffsetDateTime currentTimestamp = OffsetDateTime.now();
        StackOverflowResponse response =
            stackOverflowClient.getQuestionUpdates(questionId, link.lastCheckTime(), currentTimestamp);
        linksService.updateLastCheckTime(link.id(), currentTimestamp);

        return response.items();
    }

    @Override
    public void sendUpdatesToBot(Link link, List<StackOverflowAnswerInfo> updates) {
        for (var update : updates) {
            List<Long> tgChatsIds = tgChatsService.listAll(link.url()).stream()
                .map(TgChat::id)
                .toList();

            LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
                link.id(),
                link.url(),
                String.format(
                    "Новый ответ добавлен к вопросу %s.\n%s",
                    link.url(),
                    update.timestamp().toString()
                ),
                tgChatsIds
            );

            botClient.sendLinkUpdate(linkUpdateRequest);
        }
    }

    private long parseQuestionId(URI url) {
        Matcher matcher = STACK_OVERFLOW_QUESTION_URL_PATTERN.matcher(url.toString());
        matcher.find();
        return Long.parseLong(matcher.group(1));
    }
}
