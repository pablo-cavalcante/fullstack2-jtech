package br.com.jtech.tasklist.adapters.output.category;

import br.com.jtech.tasklist.adapters.output.repositories.CategoryRepository;
import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.output.category.CreateCategoryOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCategoryAdapter implements CreateCategoryOutputGateway {

    private final CategoryRepository repository;

    @Override
    public Category create(Category category) {
        var entity = category.toEntity();
        var saved = repository.save(entity);
        return Category.of(saved);
    }
}
