package br.com.jtech.tasklist.adapters.output.task;

import br.com.jtech.tasklist.adapters.output.repositories.TaskRepository;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.task.FindTaskByIdOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FindTaskByIdAdapter implements FindTaskByIdOutputGateway {

    private final TaskRepository repository;

    @Override
    public Optional<Task> findById(String taskId) {
        return repository.findById(UUID.fromString(taskId)).map(Task::of);
    }
}
