package br.com.jtech.tasklist.config.usecases.category;

import br.com.jtech.tasklist.adapters.output.category.FindCategoryByIdAdapter;
import br.com.jtech.tasklist.adapters.output.category.UpdateCategoryAdapter;
import br.com.jtech.tasklist.application.core.usecases.Category.UpdateCategoryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateCategoryUseCaseConfig {

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(FindCategoryByIdAdapter findCategoryByIdAdapter,
                                                       UpdateCategoryAdapter updateCategoryAdapter) {
        return new UpdateCategoryUseCase(findCategoryByIdAdapter, updateCategoryAdapter);
    }
}
