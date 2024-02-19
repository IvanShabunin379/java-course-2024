package edu.java.bot.service.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpCommand implements Command {
    @Override
    public String name() {
        return "/help";
    }

    @Override
    public String description() {
        return "вывести окно с командами";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChat().getId();

        Command startCommand = new StartCommand();
        Command helpCommand = new HelpCommand();
        Command trackCommand = new TrackCommand();
        Command untrackCommand = new UntrackCommand();
        Command listCommand = new ListCommand();

        String message =
            "Данный бот предоставляет единый способ отслеживания обновлений для множества интернет-ресурсов.\n" +
                "Вы можете вызвать необходимую команду с помощью меню, находящегося в левом нижнем углу, или с помощью ввода команды с клавиатуры\n\n" +
                "Список команд:\n" +
                commandInfo(startCommand) + ".\n\n" +
                commandInfo(helpCommand) + ".\n\n" +
                commandInfo(trackCommand) + ".\n" +
                "Важно! Имя ссылки для начала отслеживания необходимо указать через пробел после " +
                trackCommand.name() + ".\n\n" +
                commandInfo(untrackCommand) + ".\n" +
                "Важно! Имя ссылки для прекращения отслеживания необходимо указать через пробел после " +
                untrackCommand.name() + ".\n\n" +
                commandInfo(listCommand) + ".";

        return new SendMessage(String.valueOf(chatId), message);
    }

    private String commandInfo(Command command) {
        return command.name() + " - " + command.description();
    }
}
