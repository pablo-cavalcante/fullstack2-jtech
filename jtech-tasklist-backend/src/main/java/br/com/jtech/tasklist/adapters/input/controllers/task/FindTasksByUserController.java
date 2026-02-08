package br.com.jtech.tasklist.adapters.input.controllers.task;

import br.com.jtech.tasklist.adapters.input.protocols.task.TaskResponse;
import br.com.jtech.tasklist.application.core.domains.Task;
import br.com.jtech.tasklist.application.ports.input.task.FindTasksByUserInputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class FindTasksByUserController {

    private final FindTasksByUserInputGateway findTasksByUserInputGateway;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll(Authentication authentication,
                                                      @RequestParam(required = false) String categoryId) {
        var userId = authentication.getPrincipal().toString();
        List<Task> tasks;
        if (categoryId != null && !categoryId.isBlank()) {
            tasks = findTasksByUserInputGateway.findByUserIdAndCategoryId(userId, categoryId);
        } else {
            tasks = findTasksByUserInputGateway.findByUserId(userId);
        }
        return ResponseEntity.ok(TaskResponse.of(tasks));
    }
}
