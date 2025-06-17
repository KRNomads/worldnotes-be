package org.example.note.Integration;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.example.NoteServiceApplication;
import org.example.note.application.BlockService;
import org.example.note.application.NoteService;
import org.example.note.application.ProjectService;
import org.example.note.application.dto.BlockCreateParam;
import org.example.note.application.dto.BlockDto;
import org.example.note.application.dto.BlockUpdateParam;
import org.example.note.application.dto.NoteDto;
import org.example.note.application.dto.ProjectDto;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.enums.NoteType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@Transactional
@SpringBootTest(classes = NoteServiceApplication.class)
class ProjectNoteBlockIntegrationTest {

    //private static final UUID TEST_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID TEST_USER_ID = UUID.fromString("7fe1ac4a-ef1b-4c03-ac17-c7283b9d9ba5");
    @Autowired
    private ProjectService projectService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private BlockService blockService;

    @Test
    void 내_맘대로_프로젝트_노트_블럭_생성_예시() {
        // === Given ===
        String projectTitle = "에테르니움";
        String projectDescription = "중세 판타지 + SF";

        // 캐릭터 1
        String noteTitle1 = "리안 아르텔";
        NoteType noteType1 = NoteType.CHARACTER;

        List<BlockCreateParam> blockParams1 = List.of(
                new BlockCreateParam("출신", null, BlockType.TEXT, Map.of("value", "카이로스")),
                new BlockCreateParam("능력", null, BlockType.TEXT, Map.of("value", "시간 마법 사용 가능 (시간 정지, 과거 투시 등)")),
                new BlockCreateParam("성격", null, BlockType.TEXT, Map.of("value", "고지식하지만 정의감이 강하고 타인에게 헌신적"))
        );

        // 캐릭터 2
        String noteTitle2 = "엘리시아 크로웰";
        NoteType noteType2 = NoteType.CHARACTER;

        List<BlockCreateParam> blockParams2 = List.of(
                new BlockCreateParam("출신", null, BlockType.TEXT, Map.of("value", "이노바")),
                new BlockCreateParam("능력", null, BlockType.TEXT, Map.of("value", "에테르를 인공지능과 융합해 사용하는 천재 해커")),
                new BlockCreateParam("성격", null, BlockType.TEXT, Map.of("value", "냉철하고 계산적이지만 내면에는 깊은 죄책감"))
        );

        // === When ===
        ProjectDto project = projectService.create(TEST_USER_ID, projectTitle, projectDescription);

        // 1. 기본 정보 노트 수정
        NoteDto basicInfoNote = noteService.findByProjectIdAndType(TEST_USER_ID, project.id(), NoteType.BASIC_INFO).get(0);
        List<BlockDto> basicBlocks = blockService.findByNoteId(TEST_USER_ID, basicInfoNote.id());

        blockService.update(TEST_USER_ID, basicBlocks.get(0).id(), textUpdate("에테르의 심장"));
        blockService.update(TEST_USER_ID, basicBlocks.get(1).id(), textUpdate("판타지, 서사극, 디스토피아, 정치/철학 드라마, 로맨스, 전쟁물"));
        blockService.update(TEST_USER_ID, basicBlocks.get(2).id(), textUpdate("""
        에테르의 균형이 무너지고, 카이로스의 마법과 이노바의 과학이 충돌한다.
        리안은 예언에 따라 지상을 향해 내려오고, 엘리시아는 파멸을 막기 위해 적국의 마법사들과 손을 잡는다.
        테이른은 중립의 땅에서 마지막 생명의 균형을 지키기 위해 싸우고,
        오스카는 점점 인간이 아닌 존재로 변해가며 자신의 존재 의미를 찾으려 한다.
        모든 이들이 운명의 소용돌이 속에서 “에테르의 심장”으로 향한다.
    """));

        // 2. 캐릭터 1 생성 + 수정
        NoteDto note1 = noteService.create(TEST_USER_ID, project.id(), noteTitle1, noteType1);
        blockService.createMultiple(TEST_USER_ID, note1.id(), blockParams1);
        List<BlockDto> defaultBlocks1 = blockService.findByNoteId(TEST_USER_ID, note1.id());

        blockService.update(TEST_USER_ID, defaultBlocks1.get(0).id(), textUpdate("25 세"));
        blockService.update(TEST_USER_ID, defaultBlocks1.get(1).id(), textUpdate("인간"));

        // 3. 캐릭터 2 생성 + 수정
        NoteDto note2 = noteService.create(TEST_USER_ID, project.id(), noteTitle2, noteType2);
        blockService.createMultiple(TEST_USER_ID, note2.id(), blockParams2);
        List<BlockDto> defaultBlocks2 = blockService.findByNoteId(TEST_USER_ID, note2.id());

        blockService.update(TEST_USER_ID, defaultBlocks2.get(0).id(), textUpdate("28 세"));
        blockService.update(TEST_USER_ID, defaultBlocks2.get(1).id(), textUpdate("인간"));

        // === Then ===
        assertThat(project.title()).isEqualTo(projectTitle);
        // assertThat(note.title()).isEqualTo(noteTitle);
        // assertThat(createdBlocks).hasSize(3);
        // assertThat(createdBlocks.get(0).title()).isEqualTo("블럭1");
    }

    private BlockUpdateParam textUpdate(String value) {
        return new BlockUpdateParam(null, BlockType.TEXT, Map.of("value", value));
    }
}
