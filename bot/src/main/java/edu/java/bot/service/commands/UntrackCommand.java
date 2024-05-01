package edu.java.bot.service.commands;

import edu.java.bot.client.ScrapperClient;
import edu.java.dto.RemoveLinkRequest;
import edu.java.exceptions.ClientResponseException;
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
public class UntrackCommand implements Command {
    private static final String UNKNOWN_LINK_TYPE_MESSAGE = """
        Бот не может распознать ссылку, которую Вы попытались прекратить отслеживать.

        На данный момент отслеживание поддерживается только для вопросов со StackOverflow и репозиториев GitHub.
        Вводите ссылку, которую Вы хотите прекратить отслеживать, строго через один пробел после команды /untrack.
        """;
    private static final String USER_NOT_REGISTERED_MESSAGE = """
        Извините, я не могу выполнить данную команду, так как Вы ещё не зарегистрированы.

        Для начала работы с ботом необходимо вызвать команду /start.
        """;
    private static final String LINK_ALREADY_UNTRACKED_MESSAGE = "Данная ссылка отсутствует в списке отслеживаемых.";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Прошу прощения, произошла непредвиденная ошибка!";
    private static final String ONLY_COMMAND_NAME_MESSAGE =
        "Для успешного выполнения команды /untrack, вводите ссылку, которую Вы хотите прекратить отслеживать, "
            + "строго через один пробел после /untrack!";

    private final ScrapperClient scrapperClient;

    public UntrackCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String name() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        if (messageText.equals(name())) {
            return new SendMessage(String.valueOf(chatId), ONLY_COMMAND_NAME_MESSAGE);
        }

        URI potentialUntrackedLink = URI.create(messageText.split(" ")[1]);
        LinkType typeOfPotentialNewLink = checkLinkType(potentialUntrackedLink);

        if (typeOfPotentialNewLink.equals(LinkTypeChecker.LinkType.UNKNOWN)) {
            return new SendMessage(String.valueOf(chatId), UNKNOWN_LINK_TYPE_MESSAGE);
        }

        try {
            scrapperClient.untrackLink(chatId, new RemoveLinkRequest(potentialUntrackedLink));
            return new SendMessage(
                String.valueOf(chatId),
                "Ссылка " + potentialUntrackedLink + " больше не отслеживается."
            );
        } catch (ClientResponseException e) {
            String exceptionMessage;
            String exceptionReasonName = e.getApiErrorResponse().exceptionName();

            if (exceptionReasonName.equals("TgChatNotFoundException")) {
                exceptionMessage = USER_NOT_REGISTERED_MESSAGE;
            } else if (exceptionReasonName.equals("LinkNotFoundException")
                || exceptionReasonName.equals("LinkInChatNotFoundException")) {
                exceptionMessage = LINK_ALREADY_UNTRACKED_MESSAGE;
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
//        return (messageText.length() > trackCommandNameLength)
//            && (messageText.substring(0, trackCommandNameLength).equals(name() + " "));
    }
}
