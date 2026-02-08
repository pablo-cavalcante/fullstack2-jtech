package br.com.jtech.tasklist.adapters.input.controllers.Category;

import br.com.jtech.tasklist.adapters.input.protocols.category.CategoryResponse;
import br.com.jtech.tasklist.application.ports.input.category.FindCategoryByIdInputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class FindCategoryByIdController {

    private final FindCategoryByIdInputGateway findCategoryByIdInputGateway;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable String id,
                                                     Authentication authentication) {
        var userId = authentication.getPrincipal().toString();
        var category = findCategoryByIdInputGateway.findById(id, userId);
        return ResponseEntity.ok(CategoryResponse.of(category));
    }
}
