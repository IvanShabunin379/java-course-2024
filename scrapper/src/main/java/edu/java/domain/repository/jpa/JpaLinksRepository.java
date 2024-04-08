package edu.java.domain.repository.jpa;

import edu.java.domain.model.jpa.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaLinksRepository extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> findByUrl(URI url);
    List<LinkEntity> findAllByOrderByLastCheckedTime(int limit);
}
