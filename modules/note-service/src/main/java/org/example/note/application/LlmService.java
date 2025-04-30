package org.example.note.application;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.note.application.dto.BlockDto;
import org.example.note.application.dto.BlockSummary;
import org.example.note.application.dto.NoteContentDto;
import org.example.note.application.dto.NoteDto;
import org.example.note.application.dto.NoteSummary;
import org.example.note.application.dto.ProjectDto;
import org.example.note.application.dto.ProjectMetaDto;
import org.example.note.domain.enums.NoteType;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LlmService {

    private final ProjectService projectService;
    private final NoteService noteService;
    private final BlockService blockService;

    // 유저 소유 프로젝트들 조회
    public List<ProjectDto> getProjectsByUser(UUID userId) {

        List<ProjectDto> projectDtoList = projectService.findByUserId(userId);

        return projectDtoList;
    }

    // 프로젝트 메타 정보 조회
    public ProjectMetaDto projectMeta(UUID projectId) {

        // 프로젝트 조회
        ProjectDto projectDto = projectService.findById(projectId);

        // 프로젝트 전체 Note 조회
        List<NoteDto> noteDtoList = noteService.findByProjectId(projectId);

        // NoteType별 분류 및 요약으로 변환
        Map<NoteType, List<NoteSummary>> meta = noteDtoList.stream()
                .collect(Collectors.groupingBy(
                        NoteDto::type,
                        Collectors.mapping(NoteSummary::from, Collectors.toList())
                ));

        return new ProjectMetaDto(
                projectDto.projectId(),
                projectDto.title(),
                meta
        );
    }

    // 특정 노트의 내용 조회
    public NoteContentDto readNote(UUID noteId) {

        // 노트 조회
        NoteDto noteDto = noteService.findById(noteId);

        // 노트 전체 Block 조회
        List<BlockDto> blockDtoList = blockService.findByNoteId(noteId);

        // Block 요약으로 변환
        List<BlockSummary> blocks = blockDtoList.stream()
                .map(blockDto -> BlockSummary.from(blockDto))
                .toList();

        return new NoteContentDto(
                noteDto.noteId(),
                noteDto.title(),
                noteDto.type(),
                blocks
        );
    }

}
