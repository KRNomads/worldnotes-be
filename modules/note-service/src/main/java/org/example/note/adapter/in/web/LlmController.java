package org.example.note.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.example.note.application.BlockService;
import org.example.note.application.NoteService;
import org.example.note.application.ProjectService;
import org.example.note.application.dto.BlockDto;
import org.example.note.application.dto.NoteDto;
import org.example.note.application.dto.ProjectDto;
import org.example.note.domain.enums.NoteType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/LLM")
@RequiredArgsConstructor
@Tag(name = "LLM", description = "LLM 전용 API")
public class LlmController {

    private final ProjectService projectService;
    private final NoteService noteService;
    private final BlockService blockService;

    @GetMapping("/projects")
    @Operation(summary = "유저 소유 프로젝트들 조회")
    public ResponseEntity<List<ProjectDto>> getProjets(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<ProjectDto> projectDtoList = projectService.findByUserId(userId);
        return ResponseEntity.ok(projectDtoList);
    }

    @GetMapping("/projects/{projectid}/notes")
    @Operation(summary = "특정 프로젝트 노트 타입의 노트들 조회")
    public ResponseEntity<List<NoteDto>> getNotesByType(UUID projectId, NoteType type) {
        List<NoteDto> noteDtoList = noteService.findByProjectIdAndType(projectId, type);
        return ResponseEntity.ok(noteDtoList);
    }

    @GetMapping("/notes/{noteId}")
    @Operation(summary = "특정 노트의 블록들 조회")
    public ResponseEntity<List<BlockDto>> getBlocks(UUID noteId) {
        List<BlockDto> blockDtoList = blockService.findByNoteId(noteId);
        return ResponseEntity.ok(blockDtoList);
    }

}
