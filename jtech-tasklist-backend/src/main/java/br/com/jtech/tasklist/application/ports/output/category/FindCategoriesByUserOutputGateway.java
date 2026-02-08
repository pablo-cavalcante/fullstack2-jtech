package br.com.jtech.tasklist.application.ports.output.category;

import br.com.jtech.tasklist.application.core.domains.Category;

import java.util.List;

public interface FindCategoriesByUserOutputGateway {
    List<Category> findByUserId(String userId);
}
