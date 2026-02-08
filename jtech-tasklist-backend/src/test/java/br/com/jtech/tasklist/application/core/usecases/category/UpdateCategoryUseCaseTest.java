package br.com.jtech.tasklist.application.core.usecases.category;

import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.core.usecases.Category.UpdateCategoryUseCase;
import br.com.jtech.tasklist.application.ports.output.category.FindCategoryByIdOutputGateway;
import br.com.jtech.tasklist.application.ports.output.category.UpdateCategoryOutputGateway;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseTest {

    @Mock
    private FindCategoryByIdOutputGateway findCategoryByIdOutputGateway;

    @Mock
    private UpdateCategoryOutputGateway updateCategoryOutputGateway;

    private UpdateCategoryUseCase updateCategoryUseCase;

    @BeforeEach
    void setUp() {
        updateCategoryUseCase = new UpdateCategoryUseCase(findCategoryByIdOutputGateway, updateCategoryOutputGateway);
    }

    @Test
    @DisplayName("Should update category successfully")
    void shouldUpdateCategorySuccessfully() {
        var categoryId = "cat-id";
        var userId = "user-id";
        var existing = Category.builder().id(categoryId).title("Old").userId(userId).build();
        var update = Category.builder().title("New Title").description("New Desc").build();
        var updated = Category.builder().id(categoryId).title("New Title").description("New Desc").userId(userId).build();

        when(findCategoryByIdOutputGateway.findById(categoryId)).thenReturn(Optional.of(existing));
        when(updateCategoryOutputGateway.update(any(Category.class))).thenReturn(updated);

        var result = updateCategoryUseCase.update(categoryId, update, userId);

        assertThat(result.getTitle()).isEqualTo("New Title");
        verify(updateCategoryOutputGateway, times(1)).update(any(Category.class));
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException when not found")
    void shouldThrowWhenNotFound() {
        when(findCategoryByIdOutputGateway.findById("non-existent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateCategoryUseCase.update("non-existent", Category.builder().build(), "user"))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw UnauthorizedAccessException when not owner")
    void shouldThrowWhenNotOwner() {
        var category = Category.builder().id("cat-id").userId("other-user").build();

        when(findCategoryByIdOutputGateway.findById("cat-id")).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> updateCategoryUseCase.update("cat-id", Category.builder().build(), "my-user"))
                .isInstanceOf(UnauthorizedAccessException.class);

        verify(updateCategoryOutputGateway, never()).update(any());
    }
}
