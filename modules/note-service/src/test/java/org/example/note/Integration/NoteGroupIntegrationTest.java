package org.example.note.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.example.NoteServiceApplication;
import org.example.note.adapter.out.repository.NoteGroupEntryJpaRepository;
import org.example.note.adapter.out.repository.NoteGroupJpaRepository;
import org.example.note.adapter.out.repository.NoteJpaRepository;
import org.example.note.adapter.out.repository.ProjectJpaRepository;
import org.example.note.application.NoteGroupEntryService;
import org.example.note.application.NoteGroupService;
import org.example.note.application.dto.NoteGroupDto;
import org.example.note.application.dto.NoteGroupDto.EntryDto;
import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.NoteGroup;
import org.example.note.domain.entity.NoteGroupEntry;
import org.example.note.domain.entity.Project;
import org.example.note.domain.enums.GroupType;
import org.example.note.domain.enums.NoteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = NoteServiceApplication.class)
public class NoteGroupIntegrationTest {

    @Autowired
    private NoteGroupService noteGroupService;

    @Autowired
    private NoteGroupEntryService noteGroupEntryService;

    @Autowired
    private ProjectJpaRepository projectRepository;

    @Autowired
    private NoteJpaRepository noteRepository;

    @Autowired
    private NoteGroupJpaRepository noteGroupRepository;

    @Autowired
    private NoteGroupEntryJpaRepository entryRepository;

    private UUID userId;
    private UUID projectId;
    private UUID noteId;
    private Long groupId;

    @BeforeEach
    void setUp() {
        // === Given ===
        userId = UUID.randomUUID();

        Project project = Project.create(userId, "테스트 프로젝트", "테스트 프젝");
        project = projectRepository.save(project);
        projectId = project.getId();

        Note note = Note.create(project, "테스트 노트 1", NoteType.EVENT, 100);
        note = noteRepository.save(note);
        noteId = note.getId();

        // 그룹 생성
        NoteGroupDto groupDto = noteGroupService.createGroup(userId, projectId, "노트 그룹1", GroupType.TIMELINE);
        groupId = groupDto.id();
    }

    @Test
    void 그룹_생성_조회_업데이트_삭제_시나리오() {
        // === When ===
        // 1. 모든 그룹 조회
        List<NoteGroupDto> groups = noteGroupService.getGroupsByProject(projectId);

        // === Then ===
        assertThat(groups).hasSize(1);
        assertThat(groups.get(0).title()).isEqualTo("노트 그룹1");

        // === When ===
        // 2. 그룹 수정
        String updatedTitle = "업데이트된 그룹";
        NoteGroupDto updatedGroupDto = noteGroupService.updateGroup(groupId, updatedTitle);

        // === Then ===
        assertThat(updatedGroupDto.title()).isEqualTo(updatedTitle);

        // 실제 DB에서 확인
        NoteGroup savedGroup = noteGroupRepository.findById(groupId).orElseThrow();
        assertThat(savedGroup.getTitle()).isEqualTo(updatedTitle);

        // === When ===
        // 3. 그룹 삭제
        noteGroupService.deleteGroup(groupId);

        // === Then ===
        assertThat(noteGroupRepository.findById(groupId)).isEmpty();
    }

    @Test
    void 그룹에_노트_엔트리_추가_조회_업데이트_삭제_시나리오() {
        // === When ===
        // 1. 그룹에 노트 엔트리 추가
        EntryDto entryDto = noteGroupEntryService.addEntry(groupId, noteId, 10, 20);

        // === Then ===
        assertThat(entryDto.noteId()).isEqualTo(noteId);
        assertThat(entryDto.positionX()).isEqualTo(10);
        assertThat(entryDto.positionY()).isEqualTo(20);

        // === When ===
        // 2. 그룹 내 엔트리 목록 조회
        List<EntryDto> entries = noteGroupEntryService.getEntriesInGroup(groupId);

        // === Then ===
        assertThat(entries).hasSize(1);
        assertThat(entries.get(0).noteId()).isEqualTo(noteId);

        // === When ===
        // 3. 엔트리 위치 업데이트
        EntryDto updatedEntryDto = noteGroupEntryService.updateEntryPosition(entries.get(0).id(), 100, 200);

        // === Then ===
        assertThat(updatedEntryDto.positionX()).isEqualTo(100);
        assertThat(updatedEntryDto.positionY()).isEqualTo(200);

        // 실제 DB에서 확인
        NoteGroupEntry savedEntry = entryRepository.findById(updatedEntryDto.id()).orElseThrow();
        assertThat(savedEntry.getPositionX()).isEqualTo(100);
        assertThat(savedEntry.getPositionY()).isEqualTo(200);

        // === When ===
        // 4. 엔트리 삭제
        noteGroupEntryService.removeEntry(updatedEntryDto.id());

        // === Then ===
        assertThat(entryRepository.findById(updatedEntryDto.id())).isEmpty();
    }

    @Test
    void 그룹에_중복_노트_엔트리_추가시_예외() {
        // === Given ===
        noteGroupEntryService.addEntry(groupId, noteId, 0, 0);

        // === When & Then ===
        assertThatThrownBy(() -> noteGroupEntryService.addEntry(groupId, noteId, 0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Note already in this group");
    }

}
