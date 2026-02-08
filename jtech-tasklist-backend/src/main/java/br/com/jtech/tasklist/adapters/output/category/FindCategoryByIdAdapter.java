package br.com.jtech.tasklist.adapters.output.category;

import br.com.jtech.tasklist.adapters.output.repositories.CategoryRepository;
import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.output.category.FindCategoryByIdOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FindCategoryByIdAdapter implements FindCategoryByIdOutputGateway {

    private final CategoryRepository repository;

    @Override
    public Optional<Category> findById(String categoryId) {
        return repository.findById(UUID.fromString(categoryId)).map(Category::of);
    }
}
