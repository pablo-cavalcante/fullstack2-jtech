package br.com.jtech.tasklist.application.ports.output.task;

import br.com.jtech.tasklist.application.core.domains.Task;

import java.util.List;

public interface FindTasksByUserOutputGateway {
    List<Task> findByUserId(String userId);
}
