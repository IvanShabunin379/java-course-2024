package edu.java.bot.service.commands;

import edu.java.bot.client.ScrapperClient;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.exceptions.ClientErrorException;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ListCommand implements Command {
    private static final String EMPTY_LIST_MESSAGE = "Список отслеживаемых Вами ссылок пуст.";
    private static final String USER_NOT_REGISTERED_MESSAGE = """
        Извините, я не могу выполнить данную команду, так как Вы ещё не зарегистрированы.

        Для начала работы с ботом необходимо вызвать команду /start.
        """;

    private final ScrapperClient scrapperClient;

    public ListCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String name() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChatId();

        try {
            ListLinksResponse listLinksResponse = scrapperClient.getLinks(chatId);
            List<LinkResponse> links = listLinksResponse.links();

            if (links == null || listLinksResponse.size() == 0) {
                return new SendMessage(String.valueOf(chatId), EMPTY_LIST_MESSAGE);
            }

            StringBuilder linksListMessage = new StringBuilder("Отслеживаемые Вами ссылки:\n");
            List<String> urls = links.stream()
                .map(link -> link.url().toString())
                .toList();
            linksListMessage.append(String.join("\n\n", urls));

            return new SendMessage(String.valueOf(chatId), linksListMessage.toString());
        } catch (ClientErrorException e) {
            return new SendMessage(String.valueOf(chatId), USER_NOT_REGISTERED_MESSAGE);
        }
    }
}
