package org.example.note.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.example.note.adapter.in.web.request.TagCreateRequest;
import org.example.note.adapter.in.web.request.TagUpdateRequest;
import org.example.note.application.TagService;
import org.example.note.application.dto.TagDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Tag", description = "태그")
public class TagController {

    private final TagService tagService;

    @GetMapping("/{projectId}/tags/{tagId}")
    @Operation(summary = "태그 단일 조회")
    public ResponseEntity<TagDto> getTag(@PathVariable UUID tagId) {
        TagDto tagDto = tagService.getTag(tagId);
        return ResponseEntity.ok(tagDto);
    }

    @GetMapping("/{projectId}/tags")
    @Operation(summary = "모든 태그 조회")
    public ResponseEntity<List<TagDto>> getAllTags(@PathVariable UUID projectId) {
        List<TagDto> tagList = tagService.findAllTagsByProject(projectId);
        return ResponseEntity.ok(tagList);
    }

    @PostMapping("/{projectId}/tags")
    @Operation(summary = "태그 생성")
    public ResponseEntity<TagDto> createTag(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID projectId,
            @RequestBody TagCreateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        TagDto tagDto = tagService.createTag(userId, projectId, request.name(), request.color());
        return ResponseEntity.ok(tagDto);
    }

    @PostMapping("/{projectId}/tags/{tagId}")
    @Operation(summary = "태그 업데이트")
    public ResponseEntity<TagDto> createTag(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID projectId,
            @PathVariable UUID tagId,
            @RequestBody TagUpdateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        TagDto tagDto = tagService.updateTag(projectId, tagId, request.name(), request.color());
        return ResponseEntity.ok(tagDto);
    }

    @DeleteMapping("/{projectId}/tags/{tagId}")
    @Operation(summary = "태그 삭제")
    public ResponseEntity<Void> deleteTag(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID projectId,
            @PathVariable UUID tagId) {
        tagService.deleteTag(projectId, tagId);
        return ResponseEntity.noContent().build();
    }

}
