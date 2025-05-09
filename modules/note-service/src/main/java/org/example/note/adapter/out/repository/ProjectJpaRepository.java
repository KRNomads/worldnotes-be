package org.example.note.adapter.out.repository;

import java.util.List;
import java.util.UUID;

import org.example.note.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, UUID> {

    @Query("""
        SELECT pm.project
        FROM ProjectMember pm
        WHERE pm.userId = :userId AND pm.role = org.example.note.domain.enums.MemberRole.OWNER
    """)
    List<Project> findProjectsOwnedByUser(@Param("userId") UUID userId);

}
