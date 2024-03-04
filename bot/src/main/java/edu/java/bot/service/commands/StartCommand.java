package edu.java.bot.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements Command {
    private static final String START_MESSAGE =
        "Ваша регистрация прошла успешно и теперь Вы можете пользоваться данным ботом."
            + "\nЧтобы посмотреть список доступных команд, введите команду /help или откройте меню.";

    @Override
    public String name() {
        return "/start";
    }

    @Override
    public String description() {
        return "зарегистрировать пользователя";
    }

    // Добавить сообщение на случай, если пользователь уже был зарегистрирован.
    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChat().getId();

        String userName = update.getMessage().getChat().getFirstName();
        String helloMessage = "Здравствуйте, " + userName + "! ";
        return new SendMessage(String.valueOf(chatId), helloMessage + START_MESSAGE);
    }
}
