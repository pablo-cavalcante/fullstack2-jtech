package br.com.jtech.tasklist.adapters.output.category;

import br.com.jtech.tasklist.adapters.output.repositories.CategoryRepository;
import br.com.jtech.tasklist.application.ports.output.category.DeleteCategoryOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteCategoryAdapter implements DeleteCategoryOutputGateway {

    private final CategoryRepository repository;

    @Override
    public void delete(String categoryId) {
        repository.deleteById(UUID.fromString(categoryId));
    }
}
