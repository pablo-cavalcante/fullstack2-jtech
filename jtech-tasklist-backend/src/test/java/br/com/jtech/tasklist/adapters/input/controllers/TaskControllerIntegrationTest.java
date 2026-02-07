package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.adapters.input.protocols.login.LoginRequest;
import br.com.jtech.tasklist.adapters.input.protocols.login.RegisterRequest;
import br.com.jtech.tasklist.adapters.input.protocols.task.TaskRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String getAuthToken() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .name("Task Test User")
                .email("tasktest-" + System.nanoTime() + "@test.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        var loginRequest = LoginRequest.builder()
                .email(registerRequest.getEmail())
                .password("password123")
                .build();

        var loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();

        var authResponse = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        return authResponse.get("token").asText();
    }

    @Test
    @DisplayName("Should create a task successfully")
    @Order(1)
    void shouldCreateTask() throws Exception {
        var token = getAuthToken();
        var request = TaskRequest.builder()
                .title("Integration Test Task")
                .description("Testing task creation")
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.description").value("Testing task creation"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("Should fail to create task without authentication")
    @Order(2)
    void shouldFailCreateTaskWithoutAuth() throws Exception {
        var request = TaskRequest.builder()
                .title("Unauthorized Task")
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should list user tasks")
    @Order(3)
    void shouldListUserTasks() throws Exception {
        var token = getAuthToken();

        var request = TaskRequest.builder()
                .title("List Test Task")
                .description("For listing")
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/v1/tasks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("List Test Task"));
    }

    @Test
    @DisplayName("Should get task by id")
    @Order(4)
    void shouldGetTaskById() throws Exception {
        var token = getAuthToken();

        var request = TaskRequest.builder()
                .title("Find By Id Task")
                .build();

        var createResult = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        var taskId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        mockMvc.perform(get("/api/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Find By Id Task"));
    }

    @Test
    @DisplayName("Should update task")
    @Order(5)
    void shouldUpdateTask() throws Exception {
        var token = getAuthToken();

        var createRequest = TaskRequest.builder()
                .title("Original Title")
                .description("Original Description")
                .build();

        var createResult = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn();

        var taskId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        var updateRequest = TaskRequest.builder()
                .title("Updated Title")
                .description("Updated Description")
                .completed(true)
                .build();

        mockMvc.perform(put("/api/v1/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    @DisplayName("Should delete task")
    @Order(6)
    void shouldDeleteTask() throws Exception {
        var token = getAuthToken();

        var request = TaskRequest.builder()
                .title("Task to Delete")
                .build();

        var createResult = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        var taskId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        mockMvc.perform(delete("/api/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 for non-existent task")
    @Order(7)
    void shouldReturn404ForNonExistentTask() throws Exception {
        var token = getAuthToken();
        var fakeId = "00000000-0000-0000-0000-000000000000";

        mockMvc.perform(get("/api/v1/tasks/" + fakeId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should fail to create task with empty title")
    @Order(8)
    void shouldFailCreateTaskWithEmptyTitle() throws Exception {
        var token = getAuthToken();
        var request = TaskRequest.builder()
                .title("")
                .build();

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
