package br.com.jtech.tasklist.adapters.input.controllers.Category;

import br.com.jtech.tasklist.adapters.input.protocols.category.CategoryRequest;
import br.com.jtech.tasklist.adapters.input.protocols.category.CategoryResponse;
import br.com.jtech.tasklist.application.core.domains.Category;
import br.com.jtech.tasklist.application.ports.input.category.CreateCategoryInputGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CreateCategoryController {

    private final CreateCategoryInputGateway createCategoryInputGateway;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request,
                                                   Authentication authentication) {
        var category = Category.of(request);
        category.setUserId(authentication.getPrincipal().toString());
        var created = createCategoryInputGateway.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse.of(created));
    }
}
