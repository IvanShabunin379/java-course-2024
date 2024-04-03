package edu.java.domain.repository;

import edu.java.domain.model.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface LinksRepository {
    void add(URI url);

    void removeById(long id);

    void removeByUrl(URI url);

    List<Link> findAll();

    Optional<Link> findById(long id);

    Optional<Link> findByUrl(URI url);
}
