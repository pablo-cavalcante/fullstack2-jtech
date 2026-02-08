package br.com.jtech.tasklist.config.usecases.task;

import br.com.jtech.tasklist.adapters.output.task.FindTaskByIdAdapter;
import br.com.jtech.tasklist.adapters.output.task.UpdateTaskAdapter;
import br.com.jtech.tasklist.application.core.usecases.Task.UpdateTaskUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateTaskUseCaseConfig {

    @Bean
    public UpdateTaskUseCase updateTaskUseCase(FindTaskByIdAdapter findTaskByIdAdapter,
                                               UpdateTaskAdapter updateTaskAdapter) {
        return new UpdateTaskUseCase(findTaskByIdAdapter, updateTaskAdapter);
    }
}
