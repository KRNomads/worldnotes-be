package org.example.note.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.example.note.adapter.in.web.request.BlockCreateRequest;
import org.example.note.adapter.in.web.request.BlockUpdateRequest;
import org.example.note.adapter.in.web.request.BlocksCreateRequest;
import org.example.note.application.BlockService;
import org.example.note.application.dto.BlockDto;
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
@RequestMapping("/api/v1/blocks")
@RequiredArgsConstructor
@Tag(name = "Block", description = "블록")
public class BlockController {

    private final BlockService blockService;

    @GetMapping("/note/{noteId}")
    @Operation(summary = "노트 전체 블록 조회")
    public ResponseEntity<List<BlockDto>> getBlocksByNote(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID noteId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<BlockDto> blockDtoList = blockService.findByNoteId(userId, noteId);
        return ResponseEntity.ok(blockDtoList);
    }

    @PostMapping("/block")
    @Operation(summary = "새 단일 블록 생성", description = "새 블록 하나를 생성합니다.")
    public ResponseEntity<BlockDto> createBlock(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BlockCreateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        BlockDto blockDto = blockService.create(userId, request.noteId(), request.toParam());
        return ResponseEntity.ok(blockDto);
    }

    @PostMapping("/blocks")
    @Operation(summary = "여러 블록 생성", description = "새 블록 여러 개를 생성합니다.")
    public ResponseEntity<List<BlockDto>> createBlocks(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BlocksCreateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<BlockDto> blockDtos = blockService.createMultiple(userId, request.blocks().get(0).noteId(), request.toParams());
        return ResponseEntity.ok(blockDtos);
    }

    @PutMapping("/{blockId}")
    @Operation(summary = "블록 업데이트")
    public ResponseEntity<BlockDto> updateBlock(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long blockId,
            @RequestBody BlockUpdateRequest request) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        BlockDto blockDto = blockService.update(userId, blockId, request.toParam());
        return ResponseEntity.ok(blockDto);
    }

    @DeleteMapping("/{blockId}")
    @Operation(summary = "블록 삭제")
    public ResponseEntity<Void> deleteBlock(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long blockId) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        blockService.delete(userId, blockId);
        return ResponseEntity.noContent().build();
    }
}
