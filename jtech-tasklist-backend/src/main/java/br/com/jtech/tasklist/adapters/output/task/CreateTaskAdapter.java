package br.com.jtech.tasklist.adapters.output.task;

import br.com.jtech.tasklist.adapters.output.repositories.TaskRepository;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.task.CreateTaskOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateTaskAdapter implements CreateTaskOutputGateway {

    private final TaskRepository repository;

    @Override
    public Task create(Task task) {
        var entity = task.toEntity();
        var saved = repository.save(entity);
        return Task.of(saved);
    }
}
