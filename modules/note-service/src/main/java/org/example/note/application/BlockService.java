package org.example.note.application;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.example.note.adapter.out.repository.BlockJpaRepository;
import org.example.note.adapter.out.repository.NoteJpaRepository;
import org.example.note.application.dto.BlockDto;
import org.example.note.domain.entity.Block;
import org.example.note.domain.entity.Note;
import org.example.note.domain.enums.BlockType;
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

    // === 생성 ===
    @Transactional
    public BlockDto create(UUID noteId, String title, Boolean isDefault, BlockType type, Map<String, Object> content,
            Integer position) {
        Note note = noteJpaRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        String finalTitle = (title == null || title.trim().isEmpty()) ? "" : title.trim();

        Block block = Block.create(
                note,
                finalTitle,
                isDefault,
                type,
                content,
                position);

        Block saved = blockJpaRepository.save(block);
        return BlockDto.from(saved);
    }

    @Transactional
    public void createDefaultBlocksFor(Note note) {
        List<BlockTemplate> templates = NoteBlockTemplates.getTemplates(note.getType());

        templates.forEach(template -> {
            Block block = Block.create(
                    note,
                    template.title(),
                    template.isDefault(),
                    template.type(),
                    template.content(),
                    template.position()
            );
            blockJpaRepository.save(block);
        });
    }

    // === 조회 ===
    @Transactional(readOnly = true)
    public List<BlockDto> findByNoteId(UUID noteId) {
        List<Block> blocks = blockJpaRepository.findByNoteId(noteId);
        return blocks.stream()
                .map(BlockDto::from)
                .toList();
    }

    // === 업데이트 ===
    @Transactional
    public BlockDto update(Long id, Map<String, Object> updateFields) {
        Block block = blockJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        if (updateFields.containsKey("title")) {
            block.updateTitle((String) updateFields.get("title"));
        }

        if (updateFields.containsKey("type")) {
            block.updateType(BlockType.valueOf((String) updateFields.get("type")));
        }

        if (updateFields.containsKey("content")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> content = (Map<String, Object>) updateFields.get("content");
            block.updateContent(content);
        }

        if (updateFields.containsKey("position")) {
            block.updatePosition((Integer) updateFields.get("position"));
        }

        blockJpaRepository.save(block);
        return BlockDto.from(block);
    }

    // === 삭제 ===
    @Transactional
    public void delete(Long id) {
        blockJpaRepository.deleteById(id);
    }

}
