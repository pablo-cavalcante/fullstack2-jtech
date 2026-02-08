package br.com.jtech.tasklist.config.usecases.category;

import br.com.jtech.tasklist.adapters.output.category.FindCategoryByIdAdapter;
import br.com.jtech.tasklist.application.core.usecases.Category.FindCategoryByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindCategoryByIdUseCaseConfig {

    @Bean
    public FindCategoryByIdUseCase findCategoryByIdUseCase(FindCategoryByIdAdapter findCategoryByIdAdapter) {
        return new FindCategoryByIdUseCase(findCategoryByIdAdapter);
    }
}
