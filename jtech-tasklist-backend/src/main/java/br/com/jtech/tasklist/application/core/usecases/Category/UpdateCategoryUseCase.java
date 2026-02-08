package br.com.jtech.tasklist.application.core.usecases.Category;

import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.input.category.UpdateCategoryInputGateway;
import br.com.jtech.tasklist.application.ports.output.category.FindCategoryByIdOutputGateway;
import br.com.jtech.tasklist.application.ports.output.category.UpdateCategoryOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.CategoryNotFoundException;
import br.com.jtech.tasklist.config.infra.exceptions.UnauthorizedAccessException;

public class UpdateCategoryUseCase implements UpdateCategoryInputGateway {

    private final FindCategoryByIdOutputGateway findCategoryByIdOutputGateway;
    private final UpdateCategoryOutputGateway updateCategoryOutputGateway;

    public UpdateCategoryUseCase(FindCategoryByIdOutputGateway findCategoryByIdOutputGateway,
                                 UpdateCategoryOutputGateway updateCategoryOutputGateway) {
        this.findCategoryByIdOutputGateway = findCategoryByIdOutputGateway;
        this.updateCategoryOutputGateway = updateCategoryOutputGateway;
    }

    @Override
    public Category update(String categoryId, Category category, String userId) {
        var existingCategory = findCategoryByIdOutputGateway.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));

        if (!existingCategory.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to update this category");
        }

        existingCategory.setTitle(category.getTitle());
        existingCategory.setDescription(category.getDescription());

        return updateCategoryOutputGateway.update(existingCategory);
    }
}
