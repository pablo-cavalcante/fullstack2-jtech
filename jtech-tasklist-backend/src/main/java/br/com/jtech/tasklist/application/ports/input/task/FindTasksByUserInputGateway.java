package br.com.jtech.tasklist.application.ports.input.task;

import br.com.jtech.tasklist.application.core.domains.Task;

import java.util.List;

public interface FindTasksByUserInputGateway {
    List<Task> findByUserId(String userId);
}
