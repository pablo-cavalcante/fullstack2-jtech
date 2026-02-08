package br.com.jtech.tasklist.config.usecases.task;

import br.com.jtech.tasklist.adapters.output.task.CreateTaskAdapter;
import br.com.jtech.tasklist.application.core.usecases.Task.CreateTaskUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateTaskUseCaseConfig {

    @Bean
    public CreateTaskUseCase createTaskUseCase(CreateTaskAdapter createTaskAdapter) {
        return new CreateTaskUseCase(createTaskAdapter);
    }
}
