package br.com.jtech.tasklist.application.core.domains;

import br.com.jtech.tasklist.adapters.input.protocols.category.CategoryRequest;
import br.com.jtech.tasklist.adapters.output.repositories.entities.CategoryEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private String id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;

    public static List<Category> of(List<CategoryEntity> entities) {
        return entities.stream().map(Category::of).toList();
    }

    public CategoryEntity toEntity() {
        var builder = CategoryEntity.builder()
                .title(getTitle())
                .description(getDescription());
        if (getId() != null) {
            builder.id(UUID.fromString(getId()));
        }
        if (getUserId() != null) {
            builder.user(UserEntity.builder().id(UUID.fromString(getUserId())).build());
        }
        return builder.build();
    }

    public static Category of(CategoryEntity entity) {
        return Category.builder()
                .id(entity.getId().toString())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .userId(entity.getUser() != null ? entity.getUser().getId().toString() : null)
                .build();
    }

    public static Category of(CategoryRequest request) {
        return Category.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
    }
}
