package org.example.note.application;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.example.note.adapter.out.repository.BlockJpaRepository;
import org.example.note.application.dto.BlockCreateParam;
import org.example.note.application.dto.BlockDto;
import org.example.note.domain.entity.Block;
import org.example.note.domain.entity.Note;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.property.BlockProperties;
import org.example.note.domain.property.TextBlockProperties;
import org.example.note.domain.template.BlockTemplate;
import org.example.note.domain.template.NoteBlockTemplates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockJpaRepository blockJpaRepository;
    private final BlockPermissionService blockPermissionService;
    private final NotePermissionService notePermissionService;

    // === 조회 ===
    @Transactional(readOnly = true)
    public List<BlockDto> findByNoteId(UUID userId, UUID noteId) {
        notePermissionService.checkIsOwner(userId, noteId);
        List<Block> blocks = blockJpaRepository.findByNoteId(noteId);
        return blocks.stream()
                .map(BlockDto::from)
                .toList();
    }

    // === 생성 ===
    @Transactional
    public BlockDto create(UUID userId, UUID noteId, BlockCreateParam param) {
        Note note = notePermissionService.getNoteIfOwner(userId, noteId);

        String title = (param.title() == null || param.title().trim().isEmpty()) ? "" : param.title().trim();

        Integer maxPosition = blockJpaRepository.findMaxPositionByNoteId(noteId).orElse(0);
        Integer newPosition = maxPosition + 100;

        Block block = Block.create(
                note,
                title,
                param.isDefault(),
                param.fieldKey(),
                param.type(),
                param.properties(),
                newPosition);

        Block saved = blockJpaRepository.save(block);
        return BlockDto.from(saved);
    }

    @Transactional
    public List<BlockDto> createMultiple(UUID userId, UUID noteId, List<BlockCreateParam> params) {
        Note note = notePermissionService.getNoteIfOwner(userId, noteId);

        Integer maxPosition = blockJpaRepository.findMaxPositionByNoteId(noteId).orElse(0);
        int[] position = {maxPosition};

        List<Block> blocks = params.stream()
                .map(param -> {
                    String title = (param.title() == null || param.title().trim().isEmpty()) ? "" : param.title().trim();
                    position[0] += 100;
                    return Block.create(
                            note,
                            title,
                            param.isDefault(),
                            param.fieldKey(),
                            param.type(),
                            param.properties(),
                            position[0]
                    );
                })
                .toList();

        List<Block> saved = blockJpaRepository.saveAll(blocks);

        return saved.stream()
                .map(BlockDto::from)
                .toList();
    }

    @Transactional
    public void createDefaultBlocksFor(Note note) {
        List<BlockTemplate> templates = NoteBlockTemplates.getTemplates(note.getType());

        List<Block> blocks = templates.stream()
                .map(template
                        -> Block.create(
                        note,
                        template.title(),
                        template.isDefault(),
                        template.fieldKey(),
                        template.type(),
                        template.properties(),
                        template.position()
                ))
                .toList();

        blockJpaRepository.saveAll(blocks);
    }

    // === 업데이트 ===
    @Transactional
    public BlockDto update(UUID userId, Long id, Map<String, Object> updateFields) {
        Block block = blockPermissionService.getBlockIfOwner(userId, id);

        if (updateFields.containsKey("title")) {
            block.updateTitle((String) updateFields.get("title"));
        }

        if (updateFields.containsKey("type")) {
            block.updateType(BlockType.valueOf((String) updateFields.get("type")));
        }

        if (updateFields.containsKey("properties")) {
            // 잘작동하는지 모름?
            BlockProperties properties = (BlockProperties) updateFields.get("properties");
            block.updateProperty(properties);
        }

        blockJpaRepository.save(block);
        return BlockDto.from(block);
    }

    @Transactional
    public List<BlockDto> updateDefaultBlocks(UUID noteId, Map<String, String> fieldValues) {
        List<Block> blocks = blockJpaRepository.findByNoteId(noteId);

        blocks.stream()
                .filter(Block::isDefault) // BlockType에 따라 업데이트 다르게 하는 로직 추가 필요
                .forEach(block -> {
                    String fieldKey = block.getFieldKey();
                    if (fieldValues.containsKey(fieldKey)) {
                        String value = fieldValues.get(fieldKey);
                        block.updateProperty(new TextBlockProperties(value));
                    }
                });

        List<Block> saved = blockJpaRepository.saveAll(blocks);

        return saved.stream()
                .map(BlockDto::from)
                .toList();
    }

    // position 업데이트 로직
    // === 삭제 ===
    @Transactional
    public void delete(UUID userId, Long id) {
        Block block = blockPermissionService.getBlockIfOwner(userId, id);
        blockJpaRepository.delete(block);
    }

}
