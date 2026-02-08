package br.com.jtech.tasklist.application.core.usecases.Category;

import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.input.category.FindCategoriesByUserInputGateway;
import br.com.jtech.tasklist.application.ports.output.category.FindCategoriesByUserOutputGateway;

import java.util.List;

public class FindCategoriesByUserUseCase implements FindCategoriesByUserInputGateway {

    private final FindCategoriesByUserOutputGateway findCategoriesByUserOutputGateway;

    public FindCategoriesByUserUseCase(FindCategoriesByUserOutputGateway findCategoriesByUserOutputGateway) {
        this.findCategoriesByUserOutputGateway = findCategoriesByUserOutputGateway;
    }

    @Override
    public List<Category> findByUserId(String userId) {
        return findCategoriesByUserOutputGateway.findByUserId(userId);
    }
}
