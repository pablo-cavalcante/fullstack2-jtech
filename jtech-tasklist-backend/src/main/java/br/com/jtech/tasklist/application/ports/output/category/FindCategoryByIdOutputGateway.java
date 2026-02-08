package br.com.jtech.tasklist.application.ports.output.category;

import br.com.jtech.tasklist.application.core.domains.Category;

import java.util.Optional;

public interface FindCategoryByIdOutputGateway {
    Optional<Category> findById(String categoryId);
}
