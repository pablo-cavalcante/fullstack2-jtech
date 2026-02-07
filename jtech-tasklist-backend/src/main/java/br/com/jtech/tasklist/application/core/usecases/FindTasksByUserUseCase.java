package br.com.jtech.tasklist.application.core.usecases;

import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.task.FindTasksByUserInputGateway;
import br.com.jtech.tasklist.application.ports.output.task.FindTasksByUserOutputGateway;

import java.util.List;

public class FindTasksByUserUseCase implements FindTasksByUserInputGateway {

    private final FindTasksByUserOutputGateway findTasksByUserOutputGateway;

    public FindTasksByUserUseCase(FindTasksByUserOutputGateway findTasksByUserOutputGateway) {
        this.findTasksByUserOutputGateway = findTasksByUserOutputGateway;
    }

    @Override
    public List<Task> findByUserId(String userId) {
        return findTasksByUserOutputGateway.findByUserId(userId);
    }
}
