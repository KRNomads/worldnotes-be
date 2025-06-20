package org.example.note.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.example.note.adapter.in.web.request.ProjectCreateRequest;
import org.example.note.adapter.in.web.request.ProjectUpdateRequest;
import org.example.note.application.ProjectService;
import org.example.note.application.dto.ProjectDto;
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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "프로젝트")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{projectId}")
    @Operation(summary = "특정 프로젝트 조회")
    public ResponseEntity<ProjectDto> getProject(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID projectId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        ProjectDto projectDto = projectService.findById(userId, projectId);
        return ResponseEntity.ok(projectDto);
    }

    @GetMapping
    @Operation(summary = "유저 전체 프로젝트 조회")
    public ResponseEntity<List<ProjectDto>> getProjectsByUser(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<ProjectDto> projectDtoList = projectService.findByUserId(userId);
        return ResponseEntity.ok(projectDtoList);
    }

    @PostMapping
    @Operation(summary = "새 프로젝트 생성", description = "새 프로젝트를 생성함")
    public ResponseEntity<ProjectDto> createProject(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProjectCreateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        ProjectDto projectDto = projectService.create(userId, request.title(), request.overview());
        return ResponseEntity.ok(projectDto);
    }

    @PatchMapping("/{projectId}")
    @Operation(summary = "프로젝트 업데이트")
    public ResponseEntity<ProjectDto> updateProject(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID projectId, @RequestBody ProjectUpdateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        ProjectDto projectDto = projectService.update(userId, projectId, request.toParam());
        return ResponseEntity.ok(projectDto);
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "프로젝트 삭제")
    public ResponseEntity<Void> deleteProject(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID projectId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        projectService.delete(userId, projectId);
        return ResponseEntity.noContent().build();
    }

}
