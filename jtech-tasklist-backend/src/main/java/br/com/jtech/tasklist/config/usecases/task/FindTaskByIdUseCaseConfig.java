package br.com.jtech.tasklist.config.usecases.task;

import br.com.jtech.tasklist.adapters.output.task.FindTaskByIdAdapter;
import br.com.jtech.tasklist.application.core.usecases.Task.FindTaskByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindTaskByIdUseCaseConfig {

    @Bean
    public FindTaskByIdUseCase findTaskByIdUseCase(FindTaskByIdAdapter findTaskByIdAdapter) {
        return new FindTaskByIdUseCase(findTaskByIdAdapter);
    }
}
