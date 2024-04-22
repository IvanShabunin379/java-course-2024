package edu.java.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatesService {
    private final TelegramBot bot;

    public void sendLinkUpdate(String description, List<Long> tgChatIds) {
        bot.sendLinkUpdate(description, tgChatIds);
    }
}
