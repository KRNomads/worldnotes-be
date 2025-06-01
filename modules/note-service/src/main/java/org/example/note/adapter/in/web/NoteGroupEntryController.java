package org.example.note.adapter.in.web;

import java.util.List;

import org.example.note.adapter.in.web.request.NoteGroupEntryAddRequest;
import org.example.note.adapter.in.web.request.NoteGroupEntryUpdateRequest;
import org.example.note.application.NoteGroupEntryService;
import org.example.note.application.dto.NoteGroupDto.EntryDto;
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
@RequestMapping("/api/v1/group-entries")
@RequiredArgsConstructor
@Tag(name = "NoteGroupEntry", description = "노트 그룹 엔트리 관리")
public class NoteGroupEntryController {

    private final NoteGroupEntryService entryService;

    @GetMapping("/group/{groupId}")
    @Operation(summary = "그룹 엔트리 목록 조회")
    public ResponseEntity<List<EntryDto>> getEntriesInGroup(@PathVariable Long groupId) {
        List<EntryDto> entryDtoList = entryService.getEntriesInGroup(groupId);
        return ResponseEntity.ok(entryDtoList);
    }

    @PostMapping
    @Operation(summary = "그룹 엔트리 추가")
    public ResponseEntity<EntryDto> addEntry(@RequestParam Long groupId,
            @RequestBody NoteGroupEntryAddRequest request) {
        EntryDto entryDto = entryService.addEntry(groupId, request.noteId(), request.positionX(), request.positionY());
        return ResponseEntity.ok(entryDto);
    }

    @PutMapping("/{entryId}/position")
    @Operation(summary = "그룹 엔트리 위치 업데이트")
    public ResponseEntity<EntryDto> updatePosition(@PathVariable Long entryId,
            @RequestBody NoteGroupEntryUpdateRequest request) {
        EntryDto entryDto = entryService.updateEntryPosition(entryId, request.positionX(), request.positionY());
        return ResponseEntity.ok(entryDto);
    }

    @DeleteMapping("/{entryId}")
    @Operation(summary = "그룹 엔트리 삭제")
    public void removeEntry(@PathVariable Long entryId) {
        entryService.removeEntry(entryId);
    }

}
