package br.com.jtech.tasklist.adapters.output.category;

import br.com.jtech.tasklist.adapters.output.repositories.CategoryRepository;
import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.output.category.UpdateCategoryOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateCategoryAdapter implements UpdateCategoryOutputGateway {

    private final CategoryRepository repository;

    @Override
    public Category update(Category category) {
        var entity = repository.findById(UUID.fromString(category.getId()))
                .orElseThrow();
        entity.setTitle(category.getTitle());
        entity.setDescription(category.getDescription());
        var saved = repository.save(entity);
        return Category.of(saved);
    }
}
