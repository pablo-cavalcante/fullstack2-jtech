package br.com.jtech.tasklist.adapters.output.task;

import br.com.jtech.tasklist.adapters.output.repositories.TaskRepository;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.task.UpdateTaskOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateTaskAdapter implements UpdateTaskOutputGateway {

    private final TaskRepository repository;

    @Override
    public Task update(Task task) {
        var entity = repository.findById(UUID.fromString(task.getId()))
                .orElseThrow();
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setCompleted(task.getCompleted());
        var saved = repository.save(entity);
        return Task.of(saved);
    }
}
