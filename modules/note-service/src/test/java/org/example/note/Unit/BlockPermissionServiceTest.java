package org.example.note.Unit;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.example.NoteServiceApplication;
import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.repository.BlockJpaRepository;
import org.example.note.application.BlockPermissionService;
import org.example.note.domain.entity.Block;
import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.Project;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.enums.NoteType;
import org.example.note.domain.exception.BlockExeption;
import org.example.note.domain.property.TextBlockProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest(classes = NoteServiceApplication.class)
@Transactional
class BlockPermissionServiceTest {

    @Autowired
    private BlockPermissionService blockPermissionService;

    @Autowired
    private BlockJpaRepository blockJpaRepository;

    @Autowired
    private EntityManager entityManager;

    private UUID ownerId;
    private UUID nonOwnerId;
    private Block block;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        nonOwnerId = UUID.randomUUID();

        Project project = Project.create(ownerId, "Test Project", "Test Description");
        Note note = Note.create(project, "Test Note", NoteType.DETAILS, 100);
        block = Block.create(note, "Test Block", "test", BlockType.TEXT, new TextBlockProperties("test content"),
                100);

        entityManager.persist(project);
        entityManager.persist(note);
        entityManager.persist(block);
        entityManager.flush();
        entityManager.clear();

    }

    @Test
    @DisplayName("소유자인 경우 블록을 반환한다")
    void getBlockIfOwner_whenUserIsOwner_returnsBlock() {
        Block result = blockPermissionService.getBlockIfOwner(ownerId, block.getId());
        assertThat(result.getId()).isEqualTo(block.getId());
    }

    @Test
    @DisplayName("소유자가 아닌 경우 예외를 던진다")
    void getBlockIfOwner_whenUserIsNotOwner_throwsException() {
        assertThatThrownBy(() -> blockPermissionService.getBlockIfOwner(nonOwnerId, block.getId()))
                .isInstanceOf(BlockExeption.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.BLOCK_FORBIDDEN);
    }

}
