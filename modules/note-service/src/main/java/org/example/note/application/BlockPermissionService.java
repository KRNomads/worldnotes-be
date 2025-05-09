package org.example.note.application;

import java.util.List;
import java.util.UUID;

import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.repository.BlockJpaRepository;
import org.example.note.domain.entity.Block;
import org.example.note.domain.enums.MemberRole;
import org.example.note.domain.exception.BlockExeption;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockPermissionService {

    private final BlockJpaRepository blockJpaRepository;

    public Block getBlockIfOwner(UUID userId, Long blockId) {
        return blockJpaRepository.findBlockWithPermission(
                userId,
                blockId,
                List.of(MemberRole.OWNER)
        ).orElseThrow(() -> new BlockExeption(ErrorCode.BLOCK_FORBIDDEN, blockId));
    }

}
