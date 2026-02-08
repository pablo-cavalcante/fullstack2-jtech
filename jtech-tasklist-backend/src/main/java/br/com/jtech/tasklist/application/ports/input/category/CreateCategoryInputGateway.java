package br.com.jtech.tasklist.application.ports.input.category;

import br.com.jtech.tasklist.application.core.domains.Category;

public interface CreateCategoryInputGateway {
    Category create(Category category);
}
