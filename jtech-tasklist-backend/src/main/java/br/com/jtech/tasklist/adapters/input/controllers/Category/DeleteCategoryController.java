package br.com.jtech.tasklist.adapters.input.controllers.Category;

import br.com.jtech.tasklist.application.ports.input.category.DeleteCategoryInputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class DeleteCategoryController {

    private final DeleteCategoryInputGateway deleteCategoryInputGateway;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                        Authentication authentication) {
        var userId = authentication.getPrincipal().toString();
        deleteCategoryInputGateway.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
