package br.com.jtech.tasklist.application.ports.output.task;

public interface DeleteTaskOutputGateway {
    void delete(String taskId);
}
