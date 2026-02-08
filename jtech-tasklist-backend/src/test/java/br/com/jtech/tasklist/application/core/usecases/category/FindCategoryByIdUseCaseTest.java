package br.com.jtech.tasklist.application.core.usecases.category;

import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.core.usecases.Category.FindCategoryByIdUseCase;
import br.com.jtech.tasklist.application.ports.output.category.FindCategoryByIdOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.CategoryNotFoundException;
import br.com.jtech.tasklist.config.infra.exceptions.UnauthorizedAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCategoryByIdUseCaseTest {

    @Mock
    private FindCategoryByIdOutputGateway findCategoryByIdOutputGateway;

    private FindCategoryByIdUseCase findCategoryByIdUseCase;

    @BeforeEach
    void setUp() {
        findCategoryByIdUseCase = new FindCategoryByIdUseCase(findCategoryByIdOutputGateway);
    }

    @Test
    @DisplayName("Should find category by id successfully")
    void shouldFindCategoryById() {
        var categoryId = "cat-id";
        var userId = "user-id";
        var category = Category.builder().id(categoryId).title("Trabalho").userId(userId).build();

        when(findCategoryByIdOutputGateway.findById(categoryId)).thenReturn(Optional.of(category));

        var result = findCategoryByIdUseCase.findById(categoryId, userId);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Trabalho");
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException when not found")
    void shouldThrowWhenNotFound() {
        when(findCategoryByIdOutputGateway.findById("non-existent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> findCategoryByIdUseCase.findById("non-existent", "user"))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw UnauthorizedAccessException when not owner")
    void shouldThrowWhenNotOwner() {
        var category = Category.builder().id("cat-id").userId("other-user").build();

        when(findCategoryByIdOutputGateway.findById("cat-id")).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> findCategoryByIdUseCase.findById("cat-id", "my-user"))
                .isInstanceOf(UnauthorizedAccessException.class);
    }
}
