package org.example.note.adapter.out.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.note.domain.entity.Note;
import org.example.note.domain.enums.MemberRole;
import org.example.note.domain.enums.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteJpaRepository extends JpaRepository<Note, UUID> {

    List<Note> findByProjectId(UUID projectId);

    List<Note> findByProjectIdAndType(UUID projectId, NoteType type);

    @Query("SELECT MAX(n.position) FROM Note n WHERE n.project.id = :projectId")
    Optional<Integer> findMaxPositionByProjectId(@Param("projectId") UUID projectId);

    @Query("""
        SELECT n
        FROM Note n
        JOIN FETCH n.project p
        JOIN p.members pm
        WHERE n.id = :noteId
          AND pm.userId = :userId
          AND pm.role IN :allowedRoles
    """)
    Optional<Note> findNoteWithPermission(@Param("userId") UUID userId,
            @Param("noteId") UUID noteId, @Param("allowedRoles") List<MemberRole> allowedRoles);

}
