package br.com.jtech.tasklist.application.ports.input.task;

public interface DeleteTaskInputGateway {
    void delete(String taskId, String userId);
}
