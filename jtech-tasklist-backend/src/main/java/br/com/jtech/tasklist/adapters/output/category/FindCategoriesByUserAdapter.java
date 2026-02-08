package br.com.jtech.tasklist.adapters.output.category;

import br.com.jtech.tasklist.adapters.output.repositories.CategoryRepository;
import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.output.category.FindCategoriesByUserOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FindCategoriesByUserAdapter implements FindCategoriesByUserOutputGateway {

    private final CategoryRepository repository;

    @Override
    public List<Category> findByUserId(String userId) {
        var entities = repository.findByUserId(UUID.fromString(userId));
        return Category.of(entities);
    }
}
