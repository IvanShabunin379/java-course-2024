package edu.java.service.link_updater;

import edu.java.domain.model.TgChat;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

/*
    * Interface for updating links
    Текущее приложение умеет добавлять, удалять и показывать список ссылок, но ничего не делает для поиска и оповещения.

    В одном из предыдущих заданий мы сделали простой планировщик, который раз в N секунд выводит запись в консоль.

    Расширьте функционал планировщика:

    - в БД ищется список ссылок, которые давно не проверялись
    - при помощи GithubClient/StackOverFlowClient проверялись обновления устаревших ссылок
    - если обновления есть, то вызывается BotClient и уведомление об обновлении уходит в приложение bot
    - если обновления нет, то в БД обновляется дата последней проверки

    Важно: планировщик должен использовать для работы интерфейсы, т.е. сущности без префикса Jdbc*.
 */
public interface LinkUpdater<T> {
    List<T> getUpdatesForLink(URI url, OffsetDateTime toTimestamp);

    void sendUpdatesToBot(List<T> updates, List<TgChat> tgChats);
}
