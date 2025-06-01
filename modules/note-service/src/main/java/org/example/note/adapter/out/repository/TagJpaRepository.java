package org.example.note.adapter.out.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.note.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagJpaRepository extends JpaRepository<Tag, UUID> {

    boolean existsByNameAndProjectId(String name, UUID projectId);

    List<Tag> findByProjectId(UUID projectId);

    Optional<Tag> findByIdAndProjectId(UUID tagId, UUID projectId);

    Optional<Tag> findByNameAndProjectId(String tagName, UUID projectId);

}
