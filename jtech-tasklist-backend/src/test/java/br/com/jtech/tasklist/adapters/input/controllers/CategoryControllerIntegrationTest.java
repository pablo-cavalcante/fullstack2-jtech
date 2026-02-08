package br.com.jtech.tasklist.adapters.input.controllers;

import br.com.jtech.tasklist.adapters.input.protocols.category.CategoryRequest;
import br.com.jtech.tasklist.adapters.input.protocols.login.LoginRequest;
import br.com.jtech.tasklist.adapters.input.protocols.login.RegisterRequest;
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
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String getAuthToken() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .name("Category Test User")
                .email("cattest-" + System.nanoTime() + "@test.com")
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
    @DisplayName("Should create a category successfully")
    @Order(1)
    void shouldCreateCategory() throws Exception {
        var token = getAuthToken();
        var request = CategoryRequest.builder()
                .title("Trabalho")
                .description("Tarefas do trabalho")
                .build();

        mockMvc.perform(post("/api/v1/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Trabalho"))
                .andExpect(jsonPath("$.description").value("Tarefas do trabalho"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("Should fail to create category without auth")
    @Order(2)
    void shouldFailCreateCategoryWithoutAuth() throws Exception {
        var request = CategoryRequest.builder()
                .title("Test")
                .build();

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should fail to create category with empty title")
    @Order(3)
    void shouldFailCreateWithEmptyTitle() throws Exception {
        var token = getAuthToken();
        var request = CategoryRequest.builder()
                .title("")
                .build();

        mockMvc.perform(post("/api/v1/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should list categories for user")
    @Order(4)
    void shouldListCategories() throws Exception {
        var token = getAuthToken();

        // Create a category first
        var request = CategoryRequest.builder()
                .title("Pessoal")
                .description("Tarefas pessoais")
                .build();

        mockMvc.perform(post("/api/v1/categories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/v1/categories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Pessoal"));
    }

    @Test
    @DisplayName("Should update a category")
    @Order(5)
    void shouldUpdateCategory() throws Exception {
        var token = getAuthToken();

        // Create
        var createRequest = CategoryRequest.builder()
                .title("Original")
                .build();

        var createResult = mockMvc.perform(post("/api/v1/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn();

        var categoryId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        // Update
        var updateRequest = CategoryRequest.builder()
                .title("Updated")
                .description("Updated desc")
                .build();

        mockMvc.perform(put("/api/v1/categories/" + categoryId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.description").value("Updated desc"));
    }

    @Test
    @DisplayName("Should delete a category")
    @Order(6)
    void shouldDeleteCategory() throws Exception {
        var token = getAuthToken();

        // Create
        var request = CategoryRequest.builder()
                .title("To Delete")
                .build();

        var createResult = mockMvc.perform(post("/api/v1/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        var categoryId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        // Delete
        mockMvc.perform(delete("/api/v1/categories/" + categoryId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // Verify deleted
        mockMvc.perform(get("/api/v1/categories/" + categoryId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
