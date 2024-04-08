package edu.java.service;

import edu.java.domain.model.jdbc.Link;
import java.net.URI;
import java.util.List;

public interface LinksService {
    Link add(long tgChatId, URI url);

    Link remove(long tgChatId, URI url);

    List<Link> listAll(long tgChatId);
}