package br.com.jtech.tasklist.application.ports.input.task;

import br.com.jtech.tasklist.application.core.domains.Task;

public interface UpdateTaskInputGateway {
    Task update(String taskId, Task task, String userId);
}
