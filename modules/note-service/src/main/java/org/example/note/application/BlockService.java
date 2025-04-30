package org.example.note.application;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.example.common.exception.ErrorCode;
import org.example.note.adapter.out.repository.BlockJpaRepository;
import org.example.note.adapter.out.repository.NoteJpaRepository;
import org.example.note.application.dto.BlockCreateParam;
import org.example.note.application.dto.BlockDto;
import org.example.note.domain.entity.Block;
import org.example.note.domain.entity.Note;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.exception.BlockExeption;
import org.example.note.domain.exception.NoteException;
import org.example.note.domain.property.BlockProperties;
import org.example.note.domain.template.BlockTemplate;
import org.example.note.domain.template.NoteBlockTemplates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockJpaRepository blockJpaRepository;
    private final NoteJpaRepository noteJpaRepository;

    // === 조회 ===
    @Transactional(readOnly = true)
    public List<BlockDto> findByNoteId(UUID noteId) {
        List<Block> blocks = blockJpaRepository.findByNoteId(noteId);
        return blocks.stream()
                .map(BlockDto::from)
                .toList();
    }

    // === 생성 ===
    @Transactional
    public BlockDto create(UUID noteId, BlockCreateParam param) {
        Note note = noteJpaRepository.findById(noteId)
                .orElseThrow(() -> new NoteException(ErrorCode.NOTE_NOT_FOUND, noteId));

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
    public List<BlockDto> createMultiple(UUID noteId, List<BlockCreateParam> params) {
        Note note = noteJpaRepository.findById(noteId)
                .orElseThrow(() -> new NoteException(ErrorCode.NOTE_NOT_FOUND, noteId));

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
    public BlockDto update(Long id, Map<String, Object> updateFields) {
        Block block = blockJpaRepository.findById(id)
                .orElseThrow(() -> new BlockExeption(ErrorCode.NOTE_NOT_FOUND, id));

        if (updateFields.containsKey("title")) {
            block.updateTitle((String) updateFields.get("title"));
        }

        if (updateFields.containsKey("type")) {
            block.updateType(BlockType.valueOf((String) updateFields.get("type")));
        }

        if (updateFields.containsKey("properties")) {
            BlockProperties properties = (BlockProperties) updateFields.get("properties");
            block.updateContent(properties);
        }

        blockJpaRepository.save(block);
        return BlockDto.from(block);
    }

    // position 업데이트 로직
    // === 삭제 ===
    @Transactional
    public void delete(Long id) {
        blockJpaRepository.deleteById(id);
    }

}
