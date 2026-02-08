package br.com.jtech.tasklist.application.core.usecases.category;

import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.core.usecases.Category.CreateCategoryUseCase;
import br.com.jtech.tasklist.application.ports.output.category.CreateCategoryOutputGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @Mock
    private CreateCategoryOutputGateway createCategoryOutputGateway;

    private CreateCategoryUseCase createCategoryUseCase;

    @BeforeEach
    void setUp() {
        createCategoryUseCase = new CreateCategoryUseCase(createCategoryOutputGateway);
    }

    @Test
    @DisplayName("Should create a category successfully")
    void shouldCreateCategorySuccessfully() {
        var category = Category.builder()
                .title("Trabalho")
                .description("Tarefas do trabalho")
                .userId("user-id-123")
                .build();

        var savedCategory = Category.builder()
                .id("cat-id-456")
                .title("Trabalho")
                .description("Tarefas do trabalho")
                .userId("user-id-123")
                .build();

        when(createCategoryOutputGateway.create(any(Category.class))).thenReturn(savedCategory);

        var result = createCategoryUseCase.create(category);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("cat-id-456");
        assertThat(result.getTitle()).isEqualTo("Trabalho");
        verify(createCategoryOutputGateway, times(1)).create(any(Category.class));
    }
}
