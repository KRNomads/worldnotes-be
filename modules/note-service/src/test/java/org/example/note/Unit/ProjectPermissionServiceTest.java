package org.example.note.Unit;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.example.NoteServiceApplication;
import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.repository.ProjectJpaRepository;
import org.example.note.application.ProjectPermissionService;
import org.example.note.domain.entity.Project;
import org.example.note.domain.exception.ProjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest(classes = NoteServiceApplication.class)
@Transactional
class ProjectPermissionServiceTest {

    @Autowired
    private ProjectPermissionService projectPermissionService;

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @Autowired
    private EntityManager entityManager;

    private UUID ownerId;
    private UUID nonOwnerId;
    private Project project;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        nonOwnerId = UUID.randomUUID();

        project = Project.create(ownerId, "테스트 프로젝트", "Test Description");

        entityManager.persist(project);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("소유자인 경우 프로젝트를 반환한다")
    void getProjectIfOwner_whenUserIsOwner_returnsProject() {
        Project result = projectPermissionService.getProjectIfOwner(ownerId, project.getId());
        assertThat(result.getId()).isEqualTo(project.getId());
    }

    @Test
    @DisplayName("소유자가 아닌 경우 예외를 던진다")
    void getProjectIfOwner_whenUserIsNotOwner_throwsException() {
        assertThatThrownBy(() -> projectPermissionService.getProjectIfOwner(nonOwnerId, project.getId()))
                .isInstanceOf(ProjectException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PROJECT_FORBIDDEN);
    }

    @Test
    @DisplayName("존재하지 않는 프로젝트인 경우 예외를 던진다")
    void getProjectIfOwner_whenProjectNotFound_throwsException() {
        UUID invalidProjectId = UUID.randomUUID();

        assertThatThrownBy(() -> projectPermissionService.getProjectIfOwner(ownerId, invalidProjectId))
                .isInstanceOf(ProjectException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PROJECT_NOT_FOUND);
    }

    @Test
    @DisplayName("멤버인 경우 프로젝트를 반환한다")
    void getProjectIfMember_whenUserIsMember_returnsProject() {
        Project result = projectPermissionService.getProjectIfMember(ownerId, project.getId());
        assertThat(result.getId()).isEqualTo(project.getId());
    }

    @Test
    @DisplayName("멤버가 아닌 경우 예외를 던진다")
    void getProjectIfMember_whenUserIsNotMember_throwsException() {
        assertThatThrownBy(() -> projectPermissionService.getProjectIfMember(nonOwnerId, project.getId()))
                .isInstanceOf(ProjectException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PROJECT_FORBIDDEN);
    }

    @Test
    @DisplayName("소유자인 경우 예외를 던지지 않는다")
    void checkIsOwner_whenUserIsOwner_doesNotThrowException() {
        projectPermissionService.checkIsOwner(ownerId, project.getId());
    }

    @Test
    @DisplayName("소유자가 아닌 경우 예외를 던진다")
    void checkIsOwner_whenUserIsNotOwner_throwsException() {
        assertThatThrownBy(() -> projectPermissionService.checkIsOwner(nonOwnerId, project.getId()))
                .isInstanceOf(ProjectException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PROJECT_FORBIDDEN);
    }
}
