package edu.java.bot.service.commands;

import edu.java.bot.client.ScrapperClient;
import edu.java.dto.AddLinkRequest;
import edu.java.exceptions.ClientErrorException;
import edu.java.utils.LinkTypeChecker;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static edu.java.utils.LinkTypeChecker.LinkType;
import static edu.java.utils.LinkTypeChecker.checkLinkType;

@Slf4j
@Component
public class TrackCommand implements Command {
    private static final String UNKNOWN_LINK_TYPE_MESSAGE = """
        Бот не может распознать ссылку, которую Вы попытались начать отслеживать.

        На данный момент отслеживание поддерживается только для вопросов со StackOverflow и репозиториев GitHub.
        Вводите ссылку, которую Вы хотите начать отслеживать, строго через один пробел после команды /track.
        """;
    private static final String USER_NOT_REGISTERED_MESSAGE = """
        Извините, я не могу выполнить данную команду, так как Вы ещё не зарегистрированы.

        Для начала работы с ботом необходимо вызвать команду /start.
        """;
    private static final String LINK_ALREADY_TRACK_MESSAGE = "Данная ссылка отслеживается";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Прошу прощения, произошла непредвиденная ошибка!";
    private static final String ONLY_COMMAND_NAME_MESSAGE =
        "Для успешного выполнения команды /track, вводите ссылку, которую Вы хотите начать отслеживать, "
            + "строго через один пробел после /track!";

    private final ScrapperClient scrapperClient;

    public TrackCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String name() {
        return "/track";
    }

    @Override
    public String description() {
        return "начать отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        if (messageText.equals(name())) {
            return new SendMessage(String.valueOf(chatId), ONLY_COMMAND_NAME_MESSAGE);
        }

        URI potentialTrackedLink = URI.create(messageText.split(" ")[1]);
        LinkType typeOfPotentialNewLink = checkLinkType(potentialTrackedLink);

        if (typeOfPotentialNewLink.equals(LinkTypeChecker.LinkType.UNKNOWN)) {
            return new SendMessage(String.valueOf(chatId), UNKNOWN_LINK_TYPE_MESSAGE);
        }

        try {
            scrapperClient.trackLink(chatId, new AddLinkRequest(potentialTrackedLink));
            return new SendMessage(String.valueOf(chatId), "Ссылка " + potentialTrackedLink + " теперь отслеживается.");
        } catch (ClientErrorException e) {
            String exceptionMessage;
            String exceptionReasonName = e.getApiErrorResponse().exceptionName();

            if (exceptionReasonName.equals("TgChatNotFoundException")) {
                exceptionMessage = USER_NOT_REGISTERED_MESSAGE;
            } else if (exceptionReasonName.equals("LinkInChatAlreadyExistsException")) {
                exceptionMessage = LINK_ALREADY_TRACK_MESSAGE;
            } else {
                log.error("Error: {}", e.getApiErrorResponse().description());
                exceptionMessage = UNEXPECTED_ERROR_MESSAGE;
            }

            return new SendMessage(String.valueOf(chatId), exceptionMessage);
        }
    }

    @Override
    public boolean supports(Update update) {
        String messageText = update.getMessage().getText();
        return messageText.split(" ")[0].equals(name());

//        int trackCommandNameLength = name().length();
//        return (messageText.equals(name()))
//            || (messageText.length() > trackCommandNameLength
//            && messageText.substring(0, trackCommandNameLength).equals(name() + " "));
    }
}
