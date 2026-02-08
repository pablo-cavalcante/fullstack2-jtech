package br.com.jtech.tasklist.adapters.input.controllers.Category;

import br.com.jtech.tasklist.adapters.input.protocols.category.CategoryRequest;
import br.com.jtech.tasklist.adapters.input.protocols.category.CategoryResponse;
import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.input.category.UpdateCategoryInputGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class UpdateCategoryController {

    private final UpdateCategoryInputGateway updateCategoryInputGateway;

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable String id,
                                                   @Valid @RequestBody CategoryRequest request,
                                                   Authentication authentication) {
        var userId = authentication.getPrincipal().toString();
        var category = Category.of(request);
        var updated = updateCategoryInputGateway.update(id, category, userId);
        return ResponseEntity.ok(CategoryResponse.of(updated));
    }
}
