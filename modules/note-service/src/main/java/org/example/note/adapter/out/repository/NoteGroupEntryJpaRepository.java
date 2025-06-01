package org.example.note.adapter.out.repository;

import java.util.List;

import org.example.note.domain.entity.NoteGroupEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteGroupEntryJpaRepository extends JpaRepository<NoteGroupEntry, Long> {

    List<NoteGroupEntry> findByGroupId(Long groupId);

}
