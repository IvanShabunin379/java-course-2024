package edu.java.service.jpa;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jpa.LinkEntity;
import edu.java.domain.model.jpa.TgChatEntity;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.domain.repository.jpa.JpaTgChatsRepository;
import edu.java.exceptions.LinkInChatAlreadyExistsException;
import edu.java.exceptions.LinkInChatNotFoundException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.exceptions.TgChatNotFoundException;
import edu.java.service.LinksService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
public class JpaLinksService implements LinksService {
    private final JpaLinksRepository linksRepository;

    private final JpaTgChatsRepository tgChatsRepository;

    @Override
    public Link add(long tgChatId, URI url) {
        TgChatEntity tgChat = tgChatsRepository.findById(tgChatId)
            .orElseThrow(TgChatNotFoundException::new);

        LinkEntity link = linksRepository.findByUrl(url)
            .orElseGet(() -> {
                LinkEntity newLink = new LinkEntity();
                newLink.setUrl(url);
                newLink.setLastCheckedTime(OffsetDateTime.now());
                return linksRepository.save(newLink);
            });

        if (tgChat.getLinks().stream().anyMatch(linkEntity -> linkEntity.getUrl().equals(url))) {
            throw new LinkInChatAlreadyExistsException();
        }

        tgChat.getLinks().add(link);
        tgChatsRepository.save(tgChat);

        return new Link(link.getId(), link.getUrl(), link.getLastCheckedTime());
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        TgChatEntity tgChat = tgChatsRepository.findById(tgChatId)
            .orElseThrow(TgChatNotFoundException::new);

        LinkEntity link = linksRepository.findByUrl(url)
            .orElseThrow(LinkNotFoundException::new);

        if (tgChat.getLinks().stream().noneMatch(linkEntity -> linkEntity.getUrl().equals(url))) {
            throw new LinkInChatNotFoundException();
        }

        tgChat.getLinks().remove(link);
        tgChatsRepository.save(tgChat);

        List<TgChatEntity> chats = link.getTgChats();
        chats.remove(tgChat);

        if (chats.isEmpty()) {
            linksRepository.delete(link);
        }

        return new Link(link.getId(), link.getUrl(), link.getLastCheckedTime());
    }

    @Override
    public void updateLastCheckTime(long id, OffsetDateTime lastCheckTime) {
        LinkEntity link = linksRepository.findById(id)
            .orElseThrow(LinkNotFoundException::new);

        link.setLastCheckedTime(lastCheckTime);

        linksRepository.save(link);
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        TgChatEntity tgChat = tgChatsRepository.findById(tgChatId)
            .orElseThrow(TgChatNotFoundException::new);

        return tgChat.getLinks().stream()
            .map(linkEntity -> new Link(linkEntity.getId(), linkEntity.getUrl(), linkEntity.getLastCheckedTime()))
            .toList();
    }

    @Override
    public List<Link> findUncheckedLinksForLongestTime(int limit) {
        return linksRepository.findAllByOrderByLastCheckedTime(limit)
            .stream()
            .map(linkEntity -> new Link(linkEntity.getId(), linkEntity.getUrl(), linkEntity.getLastCheckedTime()))
            .toList();
    }
}
