package br.com.jtech.tasklist.application.core.usecases.Category;

import br.com.jtech.tasklist.application.ports.input.category.DeleteCategoryInputGateway;
import br.com.jtech.tasklist.application.ports.output.category.DeleteCategoryOutputGateway;
import br.com.jtech.tasklist.application.ports.output.category.FindCategoryByIdOutputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.CategoryNotFoundException;
import br.com.jtech.tasklist.config.infra.exceptions.UnauthorizedAccessException;

public class DeleteCategoryUseCase implements DeleteCategoryInputGateway {

    private final FindCategoryByIdOutputGateway findCategoryByIdOutputGateway;
    private final DeleteCategoryOutputGateway deleteCategoryOutputGateway;

    public DeleteCategoryUseCase(FindCategoryByIdOutputGateway findCategoryByIdOutputGateway,
                                 DeleteCategoryOutputGateway deleteCategoryOutputGateway) {
        this.findCategoryByIdOutputGateway = findCategoryByIdOutputGateway;
        this.deleteCategoryOutputGateway = deleteCategoryOutputGateway;
    }

    @Override
    public void delete(String categoryId, String userId) {
        var category = findCategoryByIdOutputGateway.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));

        if (!category.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this category");
        }

        deleteCategoryOutputGateway.delete(categoryId);
    }
}
