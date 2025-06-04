package org.example.note.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.example.note.application.NoteTagService;
import org.example.note.application.dto.NoteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Note-Tag", description = "노트-태그 연결")
public class NoteTagController {

    private final NoteTagService noteTagService;

    @GetMapping("/projects/{projectId}/tags/{tagId}/notes")
    @Operation(summary = "태그로 연결된 노트 목록 조회")
    public ResponseEntity<List<NoteDto>> getNotesByTag(
            @PathVariable UUID projectId,
            @PathVariable UUID tagId) {
        List<NoteDto> notes = noteTagService.findNotesByTag(projectId, tagId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/notes/{noteId}/tags")
    @Operation(summary = "노트에 연결된 태그 목록 조회")
    public ResponseEntity<List<UUID>> getTagsByNote(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID noteId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<UUID> tags = noteTagService.findTagIdsByNote(userId, noteId);
        return ResponseEntity.ok(tags);
    }

    @PostMapping("/notes/{noteId}/tags/{tagId}")
    @Operation(summary = "노트에 태그 연결")
    public ResponseEntity<Void> addTagToNote(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID noteId,
            @PathVariable UUID tagId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        noteTagService.addTagToNote(userId, noteId, tagId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/notes/{noteId}/tags/{tagId}")
    @Operation(summary = "노트에서 태그 제거")
    public ResponseEntity<Void> removeTagFromNote(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID noteId,
            @PathVariable UUID tagId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        noteTagService.removeTagFromNote(userId, noteId, tagId);
        return ResponseEntity.noContent().build();
    }

}
