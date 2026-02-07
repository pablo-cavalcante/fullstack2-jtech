package br.com.jtech.tasklist.config.usecases.task;

import br.com.jtech.tasklist.adapters.output.task.DeleteTaskAdapter;
import br.com.jtech.tasklist.adapters.output.task.FindTaskByIdAdapter;
import br.com.jtech.tasklist.application.core.usecases.DeleteTaskUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteTaskUseCaseConfig {

    @Bean
    public DeleteTaskUseCase deleteTaskUseCase(FindTaskByIdAdapter findTaskByIdAdapter,
                                               DeleteTaskAdapter deleteTaskAdapter) {
        return new DeleteTaskUseCase(findTaskByIdAdapter, deleteTaskAdapter);
    }
}
