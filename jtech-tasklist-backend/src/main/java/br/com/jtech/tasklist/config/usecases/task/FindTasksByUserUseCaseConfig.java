package br.com.jtech.tasklist.config.usecases.task;

import br.com.jtech.tasklist.adapters.output.task.FindTasksByUserAdapter;
import br.com.jtech.tasklist.application.core.usecases.Task.FindTasksByUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindTasksByUserUseCaseConfig {

    @Bean
    public FindTasksByUserUseCase findTasksByUserUseCase(FindTasksByUserAdapter findTasksByUserAdapter) {
        return new FindTasksByUserUseCase(findTasksByUserAdapter);
    }
}
