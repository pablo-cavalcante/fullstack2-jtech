package br.com.jtech.tasklist.application.ports.input.task;

import br.com.jtech.tasklist.application.core.domains.Task;

public interface FindTaskByIdInputGateway {
    Task findById(String taskId, String userId);
}
