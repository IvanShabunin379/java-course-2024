package edu.java.bot.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatesService {
    private final TelegramBot bot;

    public void sendLinkUpdate(String description, List<Long> tgChatIds) {
        bot.sendLinkUpdate(description, tgChatIds);
    }
}
