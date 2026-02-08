package br.com.jtech.tasklist.application.ports.output.category;

import br.com.jtech.tasklist.application.core.domains.Category;

public interface UpdateCategoryOutputGateway {
    Category update(Category category);
}
