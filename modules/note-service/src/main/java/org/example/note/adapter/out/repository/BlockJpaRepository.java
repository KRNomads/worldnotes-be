package org.example.note.adapter.out.repository;

import java.util.List;
import java.util.UUID;

import org.example.note.domain.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockJpaRepository extends JpaRepository<Block, Long> {

    List<Block> findByNoteId(UUID noteId);
}
