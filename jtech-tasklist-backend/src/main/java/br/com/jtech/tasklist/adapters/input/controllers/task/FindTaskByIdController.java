package br.com.jtech.tasklist.adapters.input.controllers.task;

import br.com.jtech.tasklist.adapters.input.protocols.task.TaskResponse;
import br.com.jtech.tasklist.application.ports.input.task.FindTaskByIdInputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class FindTaskByIdController {

    private final FindTaskByIdInputGateway findTaskByIdInputGateway;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable String id,
                                                  Authentication authentication) {
        var userId = authentication.getPrincipal().toString();
        var task = findTaskByIdInputGateway.findById(id, userId);
        return ResponseEntity.ok(TaskResponse.of(task));
    }
}
