package org.example.note.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.example.note.adapter.in.web.request.NoteCreateRequest;
import org.example.note.adapter.in.web.request.NoteUpdateRequest;
import org.example.note.application.NoteService;
import org.example.note.application.dto.NoteDto;
import org.example.note.domain.enums.NoteType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/{id}")
    @Operation(summary = "특정 노트 조회")
    public ResponseEntity<NoteDto> getNote(@PathVariable UUID id) {
        NoteDto noteDto = noteService.findById(id);
        return ResponseEntity.ok(noteDto);
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "프로젝트 전체 노트 조회")
    public ResponseEntity<List<NoteDto>> getNotesByProject(@PathVariable UUID projectId) {
        List<NoteDto> noteDtoList = noteService.findByProjectId(projectId);
        return ResponseEntity.ok(noteDtoList);
    }

    @GetMapping("/project/{projectId}/filter")
    @Operation(summary = "노트 타입으로 필터링된 노트 조회")
    public ResponseEntity<List<NoteDto>> getNotesByProjectAndType(
            @PathVariable UUID projectId,
            @RequestParam("type") NoteType type
    ) {
        List<NoteDto> noteDtoList = noteService.findByProjectIdAndType(projectId, type);
        return ResponseEntity.ok(noteDtoList);
    }

    @PostMapping
    @Operation(summary = "새 노트 생성", description = "새 노트를 생성함")
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteCreateRequest request) {
        NoteDto noteDto = noteService.create(request.projectId(), request.title(), request.type(), request.position());
        return ResponseEntity.ok(noteDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "노트 업데이트")
    public ResponseEntity<NoteDto> updateNote(@PathVariable UUID id, @RequestBody NoteUpdateRequest request) {
        NoteDto noteDto = noteService.update(id, request.title());
        return ResponseEntity.ok(noteDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "노트 삭제")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
