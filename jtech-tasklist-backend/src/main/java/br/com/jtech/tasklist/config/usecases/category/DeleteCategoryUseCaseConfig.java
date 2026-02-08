package br.com.jtech.tasklist.config.usecases.category;

import br.com.jtech.tasklist.adapters.output.category.DeleteCategoryAdapter;
import br.com.jtech.tasklist.adapters.output.category.FindCategoryByIdAdapter;
import br.com.jtech.tasklist.application.core.usecases.Category.DeleteCategoryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteCategoryUseCaseConfig {

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(FindCategoryByIdAdapter findCategoryByIdAdapter,
                                                       DeleteCategoryAdapter deleteCategoryAdapter) {
        return new DeleteCategoryUseCase(findCategoryByIdAdapter, deleteCategoryAdapter);
    }
}
