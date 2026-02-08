package br.com.jtech.tasklist.config.usecases.category;

import br.com.jtech.tasklist.adapters.output.category.FindCategoriesByUserAdapter;
import br.com.jtech.tasklist.application.core.usecases.Category.FindCategoriesByUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindCategoriesByUserUseCaseConfig {

    @Bean
    public FindCategoriesByUserUseCase findCategoriesByUserUseCase(FindCategoriesByUserAdapter findCategoriesByUserAdapter) {
        return new FindCategoriesByUserUseCase(findCategoriesByUserAdapter);
    }
}
