package br.com.jtech.tasklist.application.ports.input.task;

import br.com.jtech.tasklist.application.core.domains.Task;

public interface CreateTaskInputGateway {
    Task create(Task task);
}
