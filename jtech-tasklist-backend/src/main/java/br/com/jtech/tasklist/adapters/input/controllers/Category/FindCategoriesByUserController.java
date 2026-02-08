package br.com.jtech.tasklist.adapters.input.controllers.Category;

import br.com.jtech.tasklist.adapters.input.protocols.category.CategoryResponse;
import br.com.jtech.tasklist.application.ports.input.category.FindCategoriesByUserInputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class FindCategoriesByUserController {

    private final FindCategoriesByUserInputGateway findCategoriesByUserInputGateway;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll(Authentication authentication) {
        var userId = authentication.getPrincipal().toString();
        var categories = findCategoriesByUserInputGateway.findByUserId(userId);
        return ResponseEntity.ok(CategoryResponse.of(categories));
    }
}
