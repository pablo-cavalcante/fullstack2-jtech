package br.com.jtech.tasklist.adapters.output.task;

import br.com.jtech.tasklist.adapters.output.repositories.TaskRepository;
import br.com.jtech.tasklist.application.ports.output.task.DeleteTaskOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteTaskAdapter implements DeleteTaskOutputGateway {

    private final TaskRepository repository;

    @Override
    public void delete(String taskId) {
        repository.deleteById(UUID.fromString(taskId));
    }
}
