package org.example.note;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProjectNoteBlockIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = {"USER"}, username = "123e4567-e89b-12d3-a456-426614174000", password = "password")
    void 통합_생성_테스트() throws Exception {

        // 1. 프로젝트 생성
        String projectJson = """
            {
                "name": "Test Project",
                "description": "Test Desc"
            }
        """;

        MvcResult projectResult = mockMvc.perform(post("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson)
        ).andExpect(status().isOk())
                .andReturn();

        String projectResponse = projectResult.getResponse().getContentAsString();
        UUID projectId = UUID.fromString(JsonPath.read(projectResponse, "$.id"));

        // 2. 노트 생성
        String noteJson = String.format("""
            {
                "projectId": "%s",
                "title": "Test Note",
                "type": "GENERAL",
                "position": 1
            }
        """, projectId);

        MvcResult noteResult = mockMvc.perform(post("/api/v1/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(noteJson)
        ).andExpect(status().isOk())
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

        mockMvc.perform(post("/api/blocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(blockJson)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Block"));
    }

}
