package br.com.jtech.tasklist.application.core.usecases.category;

import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.core.usecases.Category.DeleteCategoryUseCase;
import br.com.jtech.tasklist.application.ports.output.category.DeleteCategoryOutputGateway;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryUseCaseTest {

    @Mock
    private FindCategoryByIdOutputGateway findCategoryByIdOutputGateway;

    @Mock
    private DeleteCategoryOutputGateway deleteCategoryOutputGateway;

    private DeleteCategoryUseCase deleteCategoryUseCase;

    @BeforeEach
    void setUp() {
        deleteCategoryUseCase = new DeleteCategoryUseCase(findCategoryByIdOutputGateway, deleteCategoryOutputGateway);
    }

    @Test
    @DisplayName("Should delete category successfully")
    void shouldDeleteCategorySuccessfully() {
        var categoryId = "cat-id";
        var userId = "user-id";
        var category = Category.builder().id(categoryId).userId(userId).build();

        when(findCategoryByIdOutputGateway.findById(categoryId)).thenReturn(Optional.of(category));

        deleteCategoryUseCase.delete(categoryId, userId);

        verify(deleteCategoryOutputGateway, times(1)).delete(categoryId);
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException when not found")
    void shouldThrowWhenNotFound() {
        when(findCategoryByIdOutputGateway.findById("non-existent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteCategoryUseCase.delete("non-existent", "user"))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw UnauthorizedAccessException when not owner")
    void shouldThrowWhenNotOwner() {
        var category = Category.builder().id("cat-id").userId("other-user").build();

        when(findCategoryByIdOutputGateway.findById("cat-id")).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> deleteCategoryUseCase.delete("cat-id", "my-user"))
                .isInstanceOf(UnauthorizedAccessException.class);

        verify(deleteCategoryOutputGateway, never()).delete(any());
    }
}
