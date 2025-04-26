package org.example.note.application.dto;

public sealed interface NoteObjectDto permits ProjectDto, NoteDto, BlockDto {
    // 공통 인터페이스
}
