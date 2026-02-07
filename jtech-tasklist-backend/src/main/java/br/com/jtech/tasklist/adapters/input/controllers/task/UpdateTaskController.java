package br.com.jtech.tasklist.adapters.input.controllers.task;

import br.com.jtech.tasklist.adapters.input.protocols.task.TaskRequest;
import br.com.jtech.tasklist.adapters.input.protocols.task.TaskResponse;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.task.UpdateTaskInputGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class UpdateTaskController {

    private final UpdateTaskInputGateway updateTaskInputGateway;

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable String id,
                                                @Valid @RequestBody TaskRequest request,
                                                Authentication authentication) {
        var userId = authentication.getPrincipal().toString();
        var task = Task.of(request);
        var updated = updateTaskInputGateway.update(id, task, userId);
        return ResponseEntity.ok(TaskResponse.of(updated));
    }
}
