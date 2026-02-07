package br.com.jtech.tasklist.adapters.input.controllers.task;

import br.com.jtech.tasklist.application.ports.input.task.DeleteTaskInputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class DeleteTaskController {

    private final DeleteTaskInputGateway deleteTaskInputGateway;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                        Authentication authentication) {
        var userId = authentication.getPrincipal().toString();
        deleteTaskInputGateway.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
