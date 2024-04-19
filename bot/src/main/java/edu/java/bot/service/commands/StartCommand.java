package edu.java.bot.service.commands;

import edu.java.bot.client.ScrapperClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements Command {
    private static final String START_MESSAGE =
        "Ваша регистрация прошла успешно, и теперь Вы можете пользоваться данным ботом."
            + "\nЧтобы посмотреть список доступных команд, введите команду /help или откройте меню.";

    private static final String USER_ALREADY_REGISTERED_MESSAGE =
        "Вы уже зарегистрированы и можете пользоваться данным ботом."
            + "\nЧтобы посмотреть список доступных команд, введите команду /help или откройте меню.";

    private final ScrapperClient scrapperClient;

    public StartCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String name() {
        return "/start";
    }

    @Override
    public String description() {
        return "начать работу с данным ботом";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getChat().getFirstName();

        try {
            scrapperClient.registerChat(chatId);
            String helloMessage = "Здравствуйте, " + userName + "! ";
            return new SendMessage(String.valueOf(chatId), helloMessage + START_MESSAGE);
        } catch (WebClientResponseException e) {
            return new SendMessage(String.valueOf(chatId), userName + ", " + USER_ALREADY_REGISTERED_MESSAGE);
        }
    }
}
