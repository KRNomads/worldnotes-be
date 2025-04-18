package org.example.note.adapter.in;

import java.util.List;
import java.util.UUID;

import org.example.note.adapter.in.request.BlockCreateRequest;
import org.example.note.adapter.in.request.BlockUpdateRequest;
import org.example.note.application.BlockService;
import org.example.note.application.dto.BlockDto;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
@Tag(name = "Block", description = "블록")
public class BlockController {

    private final BlockService blockService;

    @PostMapping
    @Operation(summary = "새 블록 생성", description = "새 블록을 생성함")
    public ResponseEntity<BlockDto> createBlock(@RequestBody BlockCreateRequest request) {
        BlockDto blockDto = blockService.create(request.noteId(), request.title(), request.isDefault(), request.type(), request.content(), request.position());
        return ResponseEntity.ok(blockDto);
    }

    @GetMapping("/note/{noteId}")
    @Operation(summary = "노트 전체 블록 조회")
    public ResponseEntity<List<BlockDto>> getBlocksByNote(@PathVariable UUID noteId) {
        List<BlockDto> blockDtoList = blockService.findByNoteId(noteId);
        return ResponseEntity.ok(blockDtoList);
    }

    @PutMapping("/{id}")
    @Operation(summary = "블록 업데이트")
    public ResponseEntity<BlockDto> updateBlock(@PathVariable Long id, @RequestBody BlockUpdateRequest request) {
        BlockDto blockDto = blockService.update(id, request.title(), request.type(), request.content(), request.position());
        return ResponseEntity.ok(blockDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "블록 삭제")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        blockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
