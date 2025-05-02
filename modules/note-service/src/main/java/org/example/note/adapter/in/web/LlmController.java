package org.example.note.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.example.note.adapter.in.web.request.llm.NewCharacterRequest;
import org.example.note.adapter.in.web.request.llm.NewDetailsRequest;
import org.example.note.application.LlmService;
import org.example.note.application.dto.ProjectDto;
import org.example.note.application.dto.llm.NoteContentDto;
import org.example.note.application.dto.llm.ProjectMetaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/llm")
@RequiredArgsConstructor
@Tag(name = "LLM", description = "LLM 전용 API")
public class LlmController {

    private final LlmService llmService;

    @GetMapping("/projects")
    @Operation(summary = "유저 소유 프로젝트 조회")
    public ResponseEntity<List<ProjectDto>> getProjects(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<ProjectDto> projectDtoList = llmService.getProjectsByUser(userId);
        return ResponseEntity.ok(projectDtoList);
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "프로젝트 메타 정보 조회")
    public ResponseEntity<ProjectMetaDto> getProjectMetaInfo(@PathVariable UUID projectId) {
        ProjectMetaDto projectMetaDto = llmService.projectMeta(projectId);
        return ResponseEntity.ok(projectMetaDto);
    }

    @GetMapping("/note/{noteId}")
    @Operation(summary = "노트 조회")
    public ResponseEntity<NoteContentDto> readNote(@PathVariable UUID noteId) {
        NoteContentDto NoteContentdto = llmService.readNote(noteId);
        return ResponseEntity.ok(NoteContentdto);
    }

    @PostMapping("/note/character")
    @Operation(summary = "캐릭터 생성")
    public ResponseEntity<NoteContentDto> makeCharacter(@RequestBody NewCharacterRequest request) {
        NoteContentDto NoteContentdto = llmService.makeCharacter(request.projectId(), request.noteTitle(), request.age(), request.tribe(), request.extraFields());
        return ResponseEntity.ok(NoteContentdto);
    }

    @PostMapping("/note/details")
    @Operation(summary = "세계관 설정 생성")
    public ResponseEntity<NoteContentDto> makeWorldbuilding(@RequestBody NewDetailsRequest request) {
        NoteContentDto NoteContentdto = llmService.makeWorldbuilding(request.projectId(), request.noteTitle(), request.extraFields());
        return ResponseEntity.ok(NoteContentdto);
    }

    // 프로젝트 생성 / 수정
    // 등장인물 생성 / 수정
    // 세계관 설정 생성 / 수정
}
