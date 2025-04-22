package org.example.note;

import java.util.Arrays;
import java.util.UUID;

import org.example.NoteServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest(classes = NoteServiceApplication.class)
@AutoConfigureMockMvc
public class ProjectNoteBlockIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @Test
    @WithMockUser(
            roles = {"USER"},
            username = "123e4567-e89b-12d3-a456-426614174000",
            password = "password"
    )
    void 통합_생성_테스트() throws Exception {

        // 1. 프로젝트 생성
        String projectJson = """
            {
                "name": "Test Project",
                "description": "Test Desc"
            }
        """;

        MvcResult projectResult = mockMvc.perform(
                post("/api/v1/projects")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson)
        )
                .andExpect(status().isOk())
                .andReturn();

        String projectResponse = projectResult.getResponse().getContentAsString();
        UUID projectId = UUID.fromString(JsonPath.read(projectResponse, "$.id"));

        // 2. 노트 생성
        String noteJson = String.format("""
            {
                "projectId": "%s",
                "title": "Test Note",
                "type": "CHARACTER",
                "position": 1
            }
        """, projectId);

        MvcResult noteResult = mockMvc.perform(post("/api/v1/notes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(noteJson))
                .andDo(result -> {
                    System.out.println("NOTE 응답 상태: " + result.getResponse().getStatus());
                    System.out.println("NOTE 응답 내용: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andReturn();

        UUID noteId = UUID.fromString(JsonPath.read(noteResult.getResponse().getContentAsString(), "$.id"));

        // 3. 블록 생성
        String blockJson = String.format("""
            {
                "noteId": "%s",
                "title": "Test Block",
                "isDefault": false,
                "type": "TEXT",
                "content": {
                    "text": "Hello world"
                },
                "position": 1
            }
        """, noteId);

        mockMvc.perform(
                post("/api/v1/blocks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(blockJson)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Block"));
    }

}
