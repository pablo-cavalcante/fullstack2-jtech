package br.com.jtech.tasklist.application.core.usecases.category;

import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.core.usecases.Category.FindCategoriesByUserUseCase;
import br.com.jtech.tasklist.application.ports.output.category.FindCategoriesByUserOutputGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCategoriesByUserUseCaseTest {

    @Mock
    private FindCategoriesByUserOutputGateway findCategoriesByUserOutputGateway;

    private FindCategoriesByUserUseCase findCategoriesByUserUseCase;

    @BeforeEach
    void setUp() {
        findCategoriesByUserUseCase = new FindCategoriesByUserUseCase(findCategoriesByUserOutputGateway);
    }

    @Test
    @DisplayName("Should return categories for a user")
    void shouldReturnCategoriesForUser() {
        var userId = "user-id-123";
        var categories = List.of(
                Category.builder().id("1").title("Trabalho").userId(userId).build(),
                Category.builder().id("2").title("Pessoal").userId(userId).build()
        );

        when(findCategoriesByUserOutputGateway.findByUserId(userId)).thenReturn(categories);

        var result = findCategoriesByUserUseCase.findByUserId(userId);

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getTitle()).isEqualTo("Trabalho");
    }

    @Test
    @DisplayName("Should return empty list when no categories")
    void shouldReturnEmptyList() {
        when(findCategoriesByUserOutputGateway.findByUserId("user")).thenReturn(Collections.emptyList());

        var result = findCategoriesByUserUseCase.findByUserId("user");

        assertThat(result).isEmpty();
    }
}
