package br.com.jtech.tasklist.application.ports.output.task;

import br.com.jtech.tasklist.application.core.domains.Task;

import java.util.Optional;

public interface FindTaskByIdOutputGateway {
    Optional<Task> findById(String taskId);
}
