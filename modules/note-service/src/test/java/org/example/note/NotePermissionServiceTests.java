package org.example.note;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.example.NoteServiceApplication;
import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.repository.NoteJpaRepository;
import org.example.note.application.NotePermissionService;
import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.Project;
import org.example.note.domain.enums.NoteType;
import org.example.note.domain.exception.NoteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest(classes = NoteServiceApplication.class)
@Transactional
class NotePermissionServiceTests {

    @Autowired
    private NotePermissionService notePermissionService;

    @Autowired
    private NoteJpaRepository noteJpaRepository;

    @Autowired
    private EntityManager entityManager;

    private UUID ownerId;
    private UUID nonOwnerId;
    private Note note;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        nonOwnerId = UUID.randomUUID();

        Project project = Project.create(ownerId, "Test Project", "Test Description");
        note = Note.create(project, "Test Note", NoteType.DETAILS, 100);

        entityManager.persist(project);
        entityManager.persist(note);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("소유자인 경우 노트를 반환한다")
    void getNoteIfOwner_whenUserIsOwner_returnsNote() {
        Note result = notePermissionService.getNoteIfOwner(ownerId, note.getId());
        assertThat(result.getId()).isEqualTo(note.getId());
    }

    @Test
    @DisplayName("소유자가 아닌 경우 예외를 던진다")
    void getNoteIfOwner_whenUserIsNotOwner_throwsException() {
        assertThatThrownBy(() -> notePermissionService.getNoteIfOwner(nonOwnerId, note.getId()))
                .isInstanceOf(NoteException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOTE_FORBIDDEN);
    }

    @Test
    @DisplayName("소유자인 경우 예외를 던지지 않는다")
    void checkIsOwner_whenUserIsOwner_doesNotThrowException() {
        notePermissionService.checkIsOwner(ownerId, note.getId());
    }

    @Test
    @DisplayName("소유자가 아닌 경우 예외를 던진다")
    void checkIsOwner_whenUserIsNotOwner_throwsException() {
        assertThatThrownBy(() -> notePermissionService.checkIsOwner(nonOwnerId, note.getId()))
                .isInstanceOf(NoteException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOTE_FORBIDDEN);
    }
}
