package br.com.jtech.tasklist.config.usecases.category;

import br.com.jtech.tasklist.adapters.output.category.CreateCategoryAdapter;
import br.com.jtech.tasklist.application.core.usecases.Category.CreateCategoryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateCategoryUseCaseConfig {

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(CreateCategoryAdapter createCategoryAdapter) {
        return new CreateCategoryUseCase(createCategoryAdapter);
    }
}
