package org.example.note.application;

import org.example.note.application.dto.BlockDto;
import org.example.note.application.dto.NoteDto;
import org.example.note.application.dto.ProjectDto;
import org.example.note.application.message.BlockCreatePayload;
import org.example.note.application.message.BlockDeletePayload;
import org.example.note.application.message.BlockUpdatePayload;
import org.example.note.application.message.NoteCreatePayload;
import org.example.note.application.message.NoteDeletePayload;
import org.example.note.application.message.NoteUpdatePayload;
import org.example.note.application.message.ProjectUpdatePayload;
import org.example.note.application.message.WebSocketMessage;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MessageHandleService {

    private final ObjectMapper objectMapper;

    private final ProjectService projectService;
    private final NoteService noteService;
    private final BlockService blockService;

    /**
     * === Project Message ===
     */
    // "note_create"
    public WebSocketMessage handleNoteCreate(WebSocketMessage message) {
        NoteCreatePayload payload = objectMapper.convertValue(
                message.getPayload(), NoteCreatePayload.class);

        NoteDto createdNote = noteService.create(
                payload.getProjectId(),
                payload.getTitle(),
                payload.getType(),
                payload.getPosition()
        );

        payload.setNoteId(createdNote.id());
        payload.setTitle(createdNote.title());
        payload.setType(createdNote.type());
        payload.setPosition(createdNote.position());

        return WebSocketMessage.of(message.getType(), message.getUserId(), payload);
    }

    // "note_edit"
    public WebSocketMessage handleNoteEdit(WebSocketMessage message) {
        NoteUpdatePayload payload = objectMapper.convertValue(
                message.getPayload(), NoteUpdatePayload.class);

        NoteDto updatedNote = noteService.update(
                payload.getNoteId(),
                payload.getTitle(),
                payload.getPosition()
        );

        payload.setNoteId(updatedNote.id());
        payload.setTitle(updatedNote.title());
        payload.setPosition(updatedNote.position());

        return WebSocketMessage.of(message.getType(), message.getUserId(), payload);
    }

    // "note_delete"
    public WebSocketMessage handleNoteDelete(WebSocketMessage message) {
        NoteDeletePayload payload = objectMapper.convertValue(
                message.getPayload(), NoteDeletePayload.class);

        noteService.delete(payload.getNoteId());

        return WebSocketMessage.of(message.getType(), message.getUserId(), payload);
    }

    // "project_edit"
    public WebSocketMessage handleProjectEdit(WebSocketMessage message) {
        ProjectUpdatePayload payload = objectMapper.convertValue(
                message.getPayload(), ProjectUpdatePayload.class);

        ProjectDto updatedProject = projectService.update(
                payload.getProjectId(),
                payload.getTitle(),
                payload.getDescription()
        );

        payload.setProjectId(updatedProject.projectId());
        payload.setTitle(updatedProject.title());
        payload.setDescription(updatedProject.description());

        return WebSocketMessage.of(message.getType(), message.getUserId(), payload);
    }

    /**
     * === Note Message ===
     */
    // "block_create"
    public WebSocketMessage handleBlockCreate(WebSocketMessage message) {
        BlockCreatePayload payload = objectMapper.convertValue(
                message.getPayload(), BlockCreatePayload.class);

        BlockDto createdBlock = blockService.create(
                payload.getNoteId(),
                payload.getTitle(),
                payload.isDefault(),
                payload.getType(),
                payload.getContent(),
                payload.getPosition()
        );

        payload.setBlockId(createdBlock.blockId());
        payload.setTitle(createdBlock.title());
        payload.setDefault(createdBlock.isDefault());
        payload.setType(createdBlock.type());
        payload.setContent(createdBlock.content());
        payload.setPosition(createdBlock.position());

        return WebSocketMessage.of(message.getType(), message.getUserId(), payload);
    }

    // "block_edit"
    public WebSocketMessage handleBlockEdit(WebSocketMessage message) {
        BlockUpdatePayload payload = objectMapper.convertValue(
                message.getPayload(), BlockUpdatePayload.class);

        BlockDto updatedBlock = blockService.update(
                payload.getBlockId(),
                payload.getTitle(),
                payload.getType(),
                payload.getContent(),
                payload.getPosition()
        );

        payload.setBlockId(updatedBlock.blockId());
        payload.setTitle(updatedBlock.title());
        payload.setType(updatedBlock.type());
        payload.setContent(updatedBlock.content());
        payload.setPosition(updatedBlock.position());

        return WebSocketMessage.of(message.getType(), message.getUserId(), payload);
    }

    // "block_delete"
    public WebSocketMessage handleBlockDelete(WebSocketMessage message) {
        BlockDeletePayload payload = objectMapper.convertValue(
                message.getPayload(), BlockDeletePayload.class);

        blockService.delete(payload.getBlockId());

        return WebSocketMessage.of(message.getType(), message.getUserId(), payload);
    }

}
