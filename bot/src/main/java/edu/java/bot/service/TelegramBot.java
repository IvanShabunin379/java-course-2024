package edu.java.bot.service;

import edu.java.bot.configuration.BotConfig;
import edu.java.bot.service.commands.Command;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final List<Command> commands;

    public TelegramBot(BotConfig config, List<Command> commands) {
        this.config = config;
        this.commands = commands;

        try {
            List<BotCommand> botCommands = commands.stream()
                .map(Command::toApiCommand)
                .toList();
            execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.botName();
    }

    @Override
    public String getBotToken() {
        return config.telegramToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();

            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "Нераспознная команда!");
            for (Command command : commands) {
                if (command.supports(update)) {
                    sendMessage = command.handle(update);
                    break;
                }
            }

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Error occurred " + e.getMessage(), e);
            }
        }
    }

    public void sendLinkUpdate(String description, List<Long> tgChatIds) {
        tgChatIds.forEach(chatId -> {
            try {
                SendMessage linkUpdate = new SendMessage(String.valueOf(chatId), description);
                execute(linkUpdate);
            } catch (TelegramApiException e) {
                log.error("Error occurred " + e.getMessage(), e);
            }
        });
    }
}
