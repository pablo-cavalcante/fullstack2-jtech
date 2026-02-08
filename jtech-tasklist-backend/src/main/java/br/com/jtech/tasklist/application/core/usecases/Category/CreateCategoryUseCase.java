package br.com.jtech.tasklist.application.core.usecases.Category;

import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.input.category.CreateCategoryInputGateway;
import br.com.jtech.tasklist.application.ports.output.category.CreateCategoryOutputGateway;

public class CreateCategoryUseCase implements CreateCategoryInputGateway {

    private final CreateCategoryOutputGateway createCategoryOutputGateway;

    public CreateCategoryUseCase(CreateCategoryOutputGateway createCategoryOutputGateway) {
        this.createCategoryOutputGateway = createCategoryOutputGateway;
    }

    @Override
    public Category create(Category category) {
        return createCategoryOutputGateway.create(category);
    }
}
