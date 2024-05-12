package edu.java.service.link_updater;

import edu.java.client.StackOverflowClient;
import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.TgChat;
import edu.java.dto.LinkUpdateRequest;
import edu.java.responses.StackOverflowResponse;
import edu.java.responses.StackOverflowResponse.StackOverflowAnswerInfo;
import edu.java.service.LinksService;
import edu.java.service.TgChatsService;
import edu.java.service.link_updates_sender.LinkUpdatesSender;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Matcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.java.utils.LinkTypeChecker.STACK_OVERFLOW_QUESTION_URL_PATTERN;

@Service
@RequiredArgsConstructor
public class StackOverflowLinkUpdater implements LinkUpdater<StackOverflowAnswerInfo> {
    private final StackOverflowClient stackOverflowClient;
    private final LinkUpdatesSender updatesSender;
    private final LinksService linksService;
    private final TgChatsService tgChatsService;

    @Override
    public List<StackOverflowAnswerInfo> getUpdatesForLink(Link link) {
        long questionId = parseQuestionId(link.getUrl());

        OffsetDateTime currentTimestamp = OffsetDateTime.now();
        StackOverflowResponse response =
            stackOverflowClient.getQuestionUpdates(questionId, link.getLastCheckTime(), currentTimestamp);
        linksService.updateLastCheckTime(link.getId(), currentTimestamp);

        return response.items();
    }

    @Override
    public void sendUpdatesToBot(Link link, List<StackOverflowAnswerInfo> updates) {
        for (var update : updates) {
            List<Long> tgChatsIds = tgChatsService.listAll(link.getUrl()).stream()
                .map(TgChat::getId)
                .toList();

            LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
                link.getId(),
                link.getUrl(),
                String.format(
                    "Новый ответ добавлен к вопросу %s.\n%s",
                    link.getUrl(),
                    update.timestamp().toString()
                ),
                tgChatsIds
            );

            updatesSender.sendLinkUpdate(linkUpdateRequest);
        }
    }

    private long parseQuestionId(URI url) {
        Matcher matcher = STACK_OVERFLOW_QUESTION_URL_PATTERN.matcher(url.toString());
        matcher.find();
        return Long.parseLong(matcher.group(1));
    }
}
