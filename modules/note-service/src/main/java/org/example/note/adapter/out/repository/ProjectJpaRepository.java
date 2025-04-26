package org.example.note.adapter.out.repository;

import java.util.UUID;

import org.example.note.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, UUID> {

}
