package br.com.jtech.tasklist.adapters.output.task;

import br.com.jtech.tasklist.adapters.output.repositories.TaskRepository;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.output.task.FindTasksByUserOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FindTasksByUserAdapter implements FindTasksByUserOutputGateway {

    private final TaskRepository repository;

    @Override
    public List<Task> findByUserId(String userId) {
        var entities = repository.findByUserId(UUID.fromString(userId));
        return Task.of(entities);
    }
}
