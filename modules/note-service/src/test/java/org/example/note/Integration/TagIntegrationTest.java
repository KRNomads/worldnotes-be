package org.example.note.Integration;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.example.note.adapter.out.repository.NoteJpaRepository;
import org.example.note.adapter.out.repository.ProjectJpaRepository;
import org.example.note.adapter.out.repository.TagJpaRepository;
import org.example.note.application.NotePermissionService;
import org.example.note.application.NoteTagService;
import org.example.note.application.ProjectPermissionService;
import org.example.note.application.TagService;
import org.example.note.application.dto.NoteDto;
import org.example.note.application.dto.TagDto;
import org.example.note.domain.entity.Note;
import org.example.note.domain.entity.Project;
import org.example.note.domain.entity.Tag;
import org.example.note.domain.enums.NoteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TagIntegrationTest {

    @Autowired
    private NoteTagService noteTagService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ProjectJpaRepository projectRepository;

    @Autowired
    private NoteJpaRepository noteRepository;

    @Autowired
    private TagJpaRepository tagRepository;

    private UUID userId;
    private UUID projectId;
    private UUID noteId;

    @BeforeEach
    void setUp() {
        // === Given ===
        userId = UUID.randomUUID();

        Project project = Project.create(userId, "테스트 프로젝트", "테스트 프젝");
        project = projectRepository.save(project);
        projectId = project.getId();

        Note note = Note.create(project, "테스트 노트", NoteType.EVENT, 100);
        note = noteRepository.save(note);
        noteId = note.getId();
    }

    @Test
    void 태그_생성_노트에_추가_조회_삭제_시나리오() {
        // === Given ===
        String tagName = "SampleTag";
        String color = "#FF0000";

        // === When ===
        TagDto createdTagDto = tagService.createTag(userId, projectId, tagName, color);

        // === Then ===
        // 태그가 DB에 저장되었는지 검증
        Tag savedTag = tagRepository.findById(createdTagDto.id()).orElseThrow();
        assertThat(savedTag.getName()).isEqualTo(tagName);
        assertThat(savedTag.getColor()).isEqualTo(color);

        // === When ===
        // 노트에 태그 추가
        noteTagService.addTagToNote(userId, noteId, savedTag.getId());

        // === Then ===
        List<TagDto> tagsInNote = noteTagService.findTagsByNote(userId, noteId);
        assertThat(tagsInNote).hasSize(1);
        assertThat(tagsInNote.get(0).name()).isEqualTo(tagName);

        List<NoteDto> notesByTag = noteTagService.findNotesByTag(projectId, savedTag.getId());
        assertThat(notesByTag).hasSize(1);
        assertThat(notesByTag.get(0).id()).isEqualTo(noteId);

        // === When ===
        // 노트에서 태그 제거
        noteTagService.removeTagFromNote(userId, noteId, savedTag.getId());

        // === Then ===
        List<TagDto> tagsAfterRemove = noteTagService.findTagsByNote(userId, noteId);
        assertThat(tagsAfterRemove).isEmpty();
    }

    @Test
    void 태그_중복_생성시_예외_발생() {
        // === Given ===
        String tagName = "DuplicateTag";
        String color = "#00FF00";

        tagService.createTag(userId, projectId, tagName, color);

        // === When & Then ===
        assertThatThrownBy(() -> tagService.createTag(userId, projectId, tagName, color))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 태그입니다");
    }

    @Test
    void 태그_업데이트_정상_동작() {
        // === Given ===
        String originalName = "Original";
        String originalColor = "#0000FF";

        TagDto createdTag = tagService.createTag(userId, projectId, originalName, originalColor);

        // === When ===
        String updatedName = "UpdatedName";
        String updatedColor = "#123456";
        TagDto updatedTag = tagService.updateTag(projectId, createdTag.id(), updatedName, updatedColor);

        // === Then ===
        assertThat(updatedTag.name()).isEqualTo(updatedName);
        assertThat(updatedTag.color()).isEqualTo(updatedColor);

        // DB에 실제로 업데이트되었는지 확인
        Tag savedTag = tagRepository.findById(createdTag.id()).orElseThrow();
        assertThat(savedTag.getName()).isEqualTo(updatedName);
        assertThat(savedTag.getColor()).isEqualTo(updatedColor);
    }

    @Test
    void 태그_삭제_정상_동작() {
        // === Given ===
        String tagName = "DeleteMe";
        String color = "#999999";

        TagDto tagDto = tagService.createTag(userId, projectId, tagName, color);

        // === When ===
        tagService.deleteTag(projectId, tagDto.id());

        // === Then ===
        assertThat(tagRepository.findById(tagDto.id())).isEmpty();
    }

}
