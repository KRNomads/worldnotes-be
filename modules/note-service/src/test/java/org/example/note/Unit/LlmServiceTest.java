package org.example.note.Unit;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.example.NoteServiceApplication;
import org.example.note.application.LlmService;
import org.example.note.application.ProjectService;
import org.example.note.application.dto.ProjectDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = NoteServiceApplication.class)
public class LlmServiceTest {

    //private static final UUID TEST_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID TEST_USER_ID = UUID.fromString("7fe1ac4a-ef1b-4c03-ac17-c7283b9d9ba5");

    @Autowired
    private ProjectService projectService;

    @Autowired
    private LlmService llmService;

    private static ProjectDto project;

    @BeforeEach
    void setUp() {
        if (project == null) { // 첫 번째 테스트에서만 프로젝트 생성
            String projectTitle = "에테르니움";
            String projectDescription = "중세 판타지 + SF";
            project = projectService.create(TEST_USER_ID, projectTitle, projectDescription);
        }
    }

    @Test
    void LLM_기본_설정_수정_테스트() {
        // === Given ===

        // 기본 설정
        // 기본 필드
        String title = "에테르의 심장";
        String genre = "판타지, 서사극, 디스토피아, 정치/철학 드라마, 로맨스, 전쟁물";
        String keycontent = "에테르의 균형이 무너지고, 카이로스의 마법과 이노바의 과학이 충돌한다.\n"
                + //
                "리안은 예언에 따라 지상을 향해 내려오고, 엘리시아는 파멸을 막기 위해 적국의 마법사들과 손을 잡는다.\n"
                + //
                "테이른은 중립의 땅에서 마지막 생명의 균형을 지키기 위해 싸우고, 오스카는 점점 인간이 아닌 존재로 변해가며 자신의 존재 의미를 찾으려 한다.\n"
                + //
                "모든 이들이 운명의 소용돌이 속에서 “에테르의 심장”으로 향한다.";

        // === When ===
        llmService.updateBasicInfo(TEST_USER_ID, project.id(), title, genre, keycontent);

        // === Then === 
    }

    @Test
    void LLM_캐릭터_설정_만들기_테스트() {
        // === Given ===

        // 캐릭터 설정
        // 기본 필드
        String name = "테이른";
        String age = "약 120세";
        String tribe = "자연 정령(엘디리아족)";

        // 추가 필드
        Map<String, String> extraFields = new LinkedHashMap<>();
        extraFields.put("소속", "네라스 숲, 생명의 의회");
        extraFields.put("능력", "자연 동화, 대지/식물/동물과 소통 가능, 거대 수호 형태로 변신");
        extraFields.put("무기", "에테르 나무창, 자연의 분노");

        // === When ===
        llmService.makeCharacter(TEST_USER_ID, project.id(), name, age, tribe, extraFields);

        // === Then === 
    }

    @Test
    void LLM_세계관_설정_만들기_테스트() {
        // === Given ===

        // 세계관 설정
        // 기본 필드
        String noteName = "마법 체계";

        // 추가 필드
        Map<String, String> extraFields = new LinkedHashMap<>();
        extraFields.put("속성 분류", "에테르 마법은 정령 마법, 시간 마법, 기술 융합 마법으로 나뉜다.");
        extraFields.put("발현 방식", "시전자의 감정과 정신 집중을 통해 에테르가 형상화된다.");
        extraFields.put("제한 요소", "에테르 고갈, 감정 불안, 신체적 상처에 따라 마법 실패 가능.");
        extraFields.put("교육 시스템", "카이로스는 마법 아카데미 중심, 이노바는 실험적 마법 연구소 중심.");
        extraFields.put("사회적 영향", "마법 사용 능력이 귀족 계급 형성과 정치 권력의 핵심 요소가 된다.");

        // === When ===
        llmService.makeWorldbuilding(TEST_USER_ID, project.id(), noteName, extraFields);

        // === Then === 
    }

}
