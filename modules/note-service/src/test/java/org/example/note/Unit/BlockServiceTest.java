package org.example.note.Unit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.example.NoteServiceApplication;
import org.example.note.adapter.out.repository.BlockJpaRepository;
import org.example.note.application.BlockService;
import org.example.note.application.dto.BlockUpdateParam;
import org.example.note.domain.entity.Block;
import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.Project;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.enums.NoteType;
import org.example.note.domain.property.BlockProperties;
import org.example.note.domain.property.TextBlockProperties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest(classes = NoteServiceApplication.class)
@Transactional
public class BlockServiceTest {

    private static final UUID TEST_USER_ID = UUID.fromString("7fe1ac4a-ef1b-4c03-ac17-c7283b9d9ba5");

    @Autowired
    private BlockService blockService;

    @Autowired
    private BlockJpaRepository blockJpaRepository;

    @Autowired
    private EntityManager entityManager;

    private Block block;

    @BeforeEach
    void setUp() {

        Project project = Project.create(TEST_USER_ID, "Test Project", "Test Description");
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
    void 블록을_정상적으로_업데이트할_수_있다() {
        // === Given ===
        String updateText = "success";

        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("value", updateText);

        BlockUpdateParam param = new BlockUpdateParam(
                null, // title은 null (변경 없음)
                BlockType.TEXT,
                propertyMap
        );

        // === When ===
        blockService.update(TEST_USER_ID, block.getId(), param);

        // === Then === 
        Block updatedBlock = blockJpaRepository.findById(block.getId()).orElseThrow();
        BlockProperties props = updatedBlock.getProperties();

        assertEquals(TextBlockProperties.class, props.getClass()); // 타입 체크
        assertEquals(updateText, ((TextBlockProperties) props).getValue()); // 값 체크
    }

    // 텍스트 블록 테스트
    // 이미지 블록 테스트
    // 태그 블록 테스트
}
