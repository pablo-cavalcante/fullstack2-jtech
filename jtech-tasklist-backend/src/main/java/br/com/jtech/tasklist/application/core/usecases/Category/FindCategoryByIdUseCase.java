package br.com.jtech.tasklist.application.core.usecases.Category;

import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.input.category.FindCategoryByIdInputGateway;
import br.com.jtech.tasklist.application.ports.output.category.FindCategoryByIdOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.CategoryNotFoundException;
import br.com.jtech.tasklist.config.infra.exceptions.UnauthorizedAccessException;

public class FindCategoryByIdUseCase implements FindCategoryByIdInputGateway {

    private final FindCategoryByIdOutputGateway findCategoryByIdOutputGateway;

    public FindCategoryByIdUseCase(FindCategoryByIdOutputGateway findCategoryByIdOutputGateway) {
        this.findCategoryByIdOutputGateway = findCategoryByIdOutputGateway;
    }

    @Override
    public Category findById(String categoryId, String userId) {
        var category = findCategoryByIdOutputGateway.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));

        if (!category.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to access this category");
        }

        return category;
    }
}
