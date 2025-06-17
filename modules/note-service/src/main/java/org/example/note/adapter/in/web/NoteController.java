package org.example.note.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.example.note.adapter.in.web.request.NoteCreateRequest;
import org.example.note.adapter.in.web.request.NoteUpdateRequest;
import org.example.note.application.NoteService;
import org.example.note.application.dto.NoteDto;
import org.example.note.domain.enums.NoteType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
@Tag(name = "Note", description = "노트")
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/{noteId}")
    @Operation(summary = "특정 노트 조회")
    public ResponseEntity<NoteDto> getNote(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID noteId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        NoteDto noteDto = noteService.findById(userId, noteId);
        return ResponseEntity.ok(noteDto);
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "프로젝트 전체 노트 조회")
    public ResponseEntity<List<NoteDto>> getNotesByProject(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID projectId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<NoteDto> noteDtoList = noteService.findByProjectId(userId, projectId);
        return ResponseEntity.ok(noteDtoList);
    }

    @GetMapping("/project/{projectId}/filter")
    @Operation(summary = "노트 타입으로 필터링된 노트 조회")
    public ResponseEntity<List<NoteDto>> getNotesByProjectAndType(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID projectId,
            @RequestParam("type") NoteType type
    ) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<NoteDto> noteDtoList = noteService.findByProjectIdAndType(userId, projectId, type);
        return ResponseEntity.ok(noteDtoList);
    }

    @PostMapping
    @Operation(summary = "새 노트 생성", description = "새 노트를 생성함")
    public ResponseEntity<NoteDto> createNote(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody NoteCreateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        NoteDto noteDto = noteService.create(userId, request.projectId(), request.title(), request.type());
        return ResponseEntity.ok(noteDto);
    }

    @PatchMapping("/{noteId}")
    @Operation(summary = "노트 업데이트")
    public ResponseEntity<NoteDto> updateNote(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID noteId,
            @RequestBody NoteUpdateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        NoteDto noteDto = noteService.update(userId, noteId, request.toParam());
        return ResponseEntity.ok(noteDto);
    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "노트 삭제")
    public ResponseEntity<Void> deleteNote(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID noteId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        noteService.delete(userId, noteId);
        return ResponseEntity.noContent().build();
    }
}
