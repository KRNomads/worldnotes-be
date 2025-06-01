package org.example.note.adapter.out.repository;

import java.util.List;
import java.util.UUID;

import org.example.note.domain.entity.NoteGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteGroupJpaRepository extends JpaRepository<NoteGroup, Long> {

    List<NoteGroup> findByProjectId(UUID projectId);

}
