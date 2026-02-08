package br.com.jtech.tasklist.application.ports.input.category;

import br.com.jtech.tasklist.application.core.domains.Category;

import java.util.List;

public interface FindCategoriesByUserInputGateway {
    List<Category> findByUserId(String userId);
}
