// package org.example.note.adapter.in.ws;

// import java.util.Map;
// import java.util.UUID;

// import org.example.note.domain.enums.BlockType;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.stereotype.Service;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// /**
//  * LLM이나 다른 외부 시스템이 웹소켓 메시지를 보낼 수 있도록 하는 서비스
//  */
// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class WebSocketBroadcastService {

//     private final SimpMessagingTemplate messagingTemplate;

//     /**
//      * 블록 내용 변경 브로드캐스트
//      */
//     public void broadcastBlockUpdate(UUID noteId, Long blockId, String title, BlockType type,
//             Map<String, Object> content, Integer position, UUID userId) {

//         BlockUpdatePayload message = BlockUpdatePayload.builder()
//                 .noteId(noteId)
//                 .blockId(blockId)
//                 .title(title)
//                 .type(type)
//                 .content(content)
//                 .position(position)
//                 .userId(userId)
//                 .timestamp(System.currentTimeMillis())
//                 .build();

//         log.debug("Broadcasting block update: {}", message);
//         messagingTemplate.convertAndSend("/topic/note/" + noteId + "/blocks", message);
//     }

//     /**
//      * 블록 생성 브로드캐스트
//      */
//     public void broadcastBlockCreate(UUID noteId, Long blockId, String title, boolean isDefault,
//             BlockType type, Map<String, Object> content, Integer position, UUID userId) {

//         BlockCreateMessage message = BlockCreateMessage.builder()
//                 .noteId(noteId)
//                 .blockId(blockId)
//                 .title(title)
//                 .isDefault(isDefault)
//                 .type(type)
//                 .content(content)
//                 .position(position)
//                 .userId(userId)
//                 .build();

//         log.debug("Broadcasting block creation: {}", message);
//         messagingTemplate.convertAndSend("/topic/note/" + noteId + "/blocks", message);
//     }

//     /**
//      * 블록 삭제 브로드캐스트
//      */
//     public void broadcastBlockDelete(UUID noteId, Long blockId, UUID userId) {
//         BlockDeletePayload message = BlockDeletePayload.builder()
//                 .noteId(noteId)
//                 .blockId(blockId)
//                 .userId(userId)
//                 .build();

//         log.debug("Broadcasting block deletion: {}", message);
//         messagingTemplate.convertAndSend("/topic/note/" + noteId + "/blocks", message);
//     }

//     /**
//      * 노트 업데이트 브로드캐스트
//      */
//     public void broadcastNoteUpdate(UUID projectId, UUID noteId, String title, Integer position, UUID userId) {
//         NoteUpdatePayload message = NoteUpdatePayload.builder()
//                 .projectId(projectId)
//                 .noteId(noteId)
//                 .title(title)
//                 .position(position)
//                 .userId(userId)
//                 .build();

//         log.debug("Broadcasting note update: {}", message);
//         messagingTemplate.convertAndSend("/topic/project/" + projectId + "/notes", message);
//     }

//     /**
//      * LLM이 추천한 블록 변경 브로드캐스트
//      */
//     public void broadcastLlmSuggestion(UUID noteId, Long blockId, String title, BlockType type,
//             Map<String, Object> content, Integer position) {

//         // LLM 시스템 ID - 고정값 사용 (실제로는 시스템 설정에서 가져올 수 있음)
//         UUID systemUserId = UUID.fromString("00000000-0000-0000-0000-000000000000");

//         BlockUpdatePayload message = BlockUpdatePayload.builder()
//                 .noteId(noteId)
//                 .blockId(blockId)
//                 .title(title)
//                 .type(type)
//                 .content(content)
//                 .position(position)
//                 .userId(systemUserId)
//                 .timestamp(System.currentTimeMillis())
//                 .build();

//         log.debug("Broadcasting LLM suggestion: {}", message);
//         messagingTemplate.convertAndSend("/topic/note/" + noteId + "/llm-suggestions", message);
//     }
// }
