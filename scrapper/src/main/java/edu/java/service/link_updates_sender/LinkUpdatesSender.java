package edu.java.service.link_updates_sender;

import edu.java.dto.LinkUpdateRequest;

public interface LinkUpdatesSender {
    void sendLinkUpdate(LinkUpdateRequest update);
}
