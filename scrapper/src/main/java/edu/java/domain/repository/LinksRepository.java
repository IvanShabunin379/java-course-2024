package edu.java.domain.repository;

import edu.java.domain.model.jdbc.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface LinksRepository {
    void add(URI url);

    boolean removeById(long id);

    boolean removeByUrl(URI url);

    List<Link> findAll();

    List<Link> findUncheckedLinksForLongestTime(int count);

    Optional<Link> findById(long id);

    Optional<Link> findByUrl(URI url);

    boolean update(Link link);
}
