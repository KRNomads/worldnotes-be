package org.example.note.adapter.out.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.note.domain.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockJpaRepository extends JpaRepository<Block, Long> {

    List<Block> findByNoteId(UUID noteId);

    @Query("SELECT MAX(b.position) FROM Block b WHERE b.note.id = :noteId")
    Optional<Integer> findMaxPositionByNoteId(@Param("noteId") UUID noteId);

}
