package org.example.note.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.note.application.dto.BlockCreateParam;
import org.example.note.application.dto.BlockDto;
import org.example.note.application.dto.NoteDto;
import org.example.note.application.dto.ProjectDto;
import org.example.note.application.dto.llm.BlockSummary;
import org.example.note.application.dto.llm.NoteContentDto;
import org.example.note.application.dto.llm.NoteSummary;
import org.example.note.application.dto.llm.ProjectMetaDto;
import org.example.note.application.event.NoteEvent;
import org.example.note.domain.enums.BlockType;
import org.example.note.domain.enums.NoteType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LlmService {

    private final ApplicationEventPublisher publisher;
    private final ProjectService projectService;
    private final NoteService noteService;
    private final BlockService blockService;

    // 유저 소유 프로젝트들 조회
    public List<ProjectDto> getProjectsByUser(UUID userId) {

        List<ProjectDto> projectDtoList = projectService.findByUserId(userId);

        return projectDtoList;
    }

    // 프로젝트 메타 정보 조회
    public ProjectMetaDto projectMeta(UUID userId, UUID projectId) {

        // 프로젝트 조회
        ProjectDto projectDto = projectService.findById(userId, projectId); // 권한 체크크

        // 프로젝트 전체 Note 조회
        List<NoteDto> noteDtoList = noteService.findByProjectId(userId, projectId); // 권한 체크크

        // NoteType별 분류 및 요약으로 변환
        Map<NoteType, List<NoteSummary>> meta = noteDtoList.stream()
                .collect(Collectors.groupingBy(
                        NoteDto::type,
                        Collectors.mapping(NoteSummary::from, Collectors.toList())
                ));

        return new ProjectMetaDto(
                projectDto.id(),
                projectDto.title(),
                meta
        );
    }

    // 특정 노트의 내용 조회
    public NoteContentDto readNote(UUID userId, UUID noteId) {

        // 노트 조회
        NoteDto noteDto = noteService.findById(userId, noteId); // 권한 체크크

        // 노트 전체 Block 조회
        List<BlockDto> blockDtoList = blockService.findByNoteId(userId, noteId); // 권한 체크크

        // Block 요약으로 변환
        List<BlockSummary> blocks = blockDtoList.stream()
                .map(blockDto -> BlockSummary.from(blockDto))
                .toList();

        return new NoteContentDto(
                noteDto.id(),
                noteDto.title(),
                noteDto.type(),
                blocks);
    }

    // 캐릭터 설정 생성
    public NoteContentDto makeCharacter(UUID userId, UUID projectId, String noteTitle, String age, String tribe,
            Map<String, String> extraFields) {

        // 노트 생성
        NoteDto noteDto = noteService.create(userId, projectId, noteTitle, NoteType.CHARACTER); // 권한 체크크

        // 기본 필드 수정
        Map<String, String> defaultFieldValues = new HashMap<>();
        defaultFieldValues.put("age", age);
        defaultFieldValues.put("tribe", tribe);
        List<BlockDto> updatedBlockDtoList = blockService.updateDefaultBlocks(noteDto.id(), defaultFieldValues);

        // 추가 필드 생성
        List<BlockCreateParam> params = new ArrayList<>();
        extraFields.forEach((key, value) -> {
            params.add(new BlockCreateParam(key, null, BlockType.TEXT, Map.of("value", value)));
        });
        List<BlockDto> newBlockDtoList = blockService.createMultiple(userId, noteDto.id(), params); // 권한 체크크

        // 두 개의 블록 리스트 합치기
        List<BlockDto> combinedBlockDtoList = new ArrayList<>();
        combinedBlockDtoList.addAll(updatedBlockDtoList);
        combinedBlockDtoList.addAll(newBlockDtoList);

        // BlockDto 리스트를 BlockSummary 리스트로 변환
        List<BlockSummary> blocks = combinedBlockDtoList.stream()
                .map(BlockSummary::from)
                .collect(Collectors.toList());

        // 웹소켓 이벤트 발행
        publisher.publishEvent(NoteEvent.created(userId, noteDto));

        return new NoteContentDto(
                noteDto.id(),
                noteDto.title(),
                noteDto.type(),
                blocks);
    }

    // 세계관 설정 생성
    public NoteContentDto makeWorldbuilding(UUID userId, UUID projectId, String noteTitle, Map<String, String> extraFields) {

        // 노트 생성
        NoteDto noteDto = noteService.create(userId, projectId, noteTitle, NoteType.DETAILS);  // 권한 체크크

        // 추가 필드 생성
        List<BlockCreateParam> params = new ArrayList<>();
        extraFields.forEach((key, value) -> {
            params.add(new BlockCreateParam(key, null, BlockType.TEXT, Map.of("value", value)));
        });
        List<BlockDto> newBlockDtoList = blockService.createMultiple(userId, noteDto.id(), params); // 권한 체크크

        // BlockDto 리스트를 BlockSummary 리스트로 변환
        List<BlockSummary> blocks = newBlockDtoList.stream()
                .map(BlockSummary::from)
                .collect(Collectors.toList());

        // 웹소켓 이벤트 발행
        publisher.publishEvent(NoteEvent.created(userId, noteDto));

        return new NoteContentDto(
                noteDto.id(),
                noteDto.title(),
                noteDto.type(),
                blocks);
    }

    // 노트를 title을 기준으로 내용 업데이트 & 추가
}
