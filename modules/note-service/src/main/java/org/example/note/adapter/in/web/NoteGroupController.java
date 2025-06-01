package org.example.note.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.example.note.adapter.in.web.request.NoteGroupCreateRequest;
import org.example.note.adapter.in.web.request.NoteGroupUpdateRequest;
import org.example.note.application.NoteGroupService;
import org.example.note.application.dto.NoteGroupDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Tag(name = "NoteGroup", description = "노트 그룹 관리")
public class NoteGroupController {

    private final NoteGroupService noteGroupService;

    @GetMapping("/project/{projectId}")
    @Operation(summary = "특정 프로젝트의 모든 그룹 조회")
    public ResponseEntity<List<NoteGroupDto>> getGroupsByProject(@PathVariable UUID projectId) {
        List<NoteGroupDto> noteGroupDtoList = noteGroupService.getGroupsByProject(projectId);
        return ResponseEntity.ok(noteGroupDtoList);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "단일 그룹 조회")
    public ResponseEntity<NoteGroupDto> getGroup(@PathVariable Long groupId) {
        NoteGroupDto noteGroupDto = noteGroupService.getGroup(groupId).orElseThrow();
        return ResponseEntity.ok(noteGroupDto);
    }

    @PostMapping
    @Operation(summary = "그룹 생성")
    public ResponseEntity<NoteGroupDto> createGroup(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody NoteGroupCreateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        NoteGroupDto noteGroupDto = noteGroupService.createGroup(userId, request.projectId(), request.title(), request.type());
        return ResponseEntity.ok(noteGroupDto);
    }

    @PutMapping("/{groupId}")
    @Operation(summary = "그룹 업데이트")
    public ResponseEntity<NoteGroupDto> updateGroup(@PathVariable Long groupId,
            @RequestBody NoteGroupUpdateRequest request) {
        NoteGroupDto noteGroupDto = noteGroupService.updateGroup(groupId, request.title());
        return ResponseEntity.ok(noteGroupDto);
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "그룹 삭제")
    public void deleteGroup(@PathVariable Long groupId) {
        noteGroupService.deleteGroup(groupId);
    }

}
