package br.com.jtech.tasklist.application.core.domains;

import br.com.jtech.tasklist.adapters.input.protocols.task.TaskRequest;
import br.com.jtech.tasklist.adapters.output.repositories.entities.CategoryEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TaskEntity;
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
public class Task {

    private String id;
    private String title;
    private String description;
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;
    private String categoryId;

    public static List<Task> of(List<TaskEntity> entities) {
        return entities.stream().map(Task::of).toList();
    }

    public TaskEntity toEntity() {
        var builder = TaskEntity.builder()
                .title(getTitle())
                .description(getDescription())
                .completed(getCompleted() != null ? getCompleted() : false);
        if (getId() != null) {
            builder.id(UUID.fromString(getId()));
        }
        if (getUserId() != null) {
            builder.user(UserEntity.builder().id(UUID.fromString(getUserId())).build());
        }
        if (getCategoryId() != null) {
            builder.category(CategoryEntity.builder().id(UUID.fromString(getCategoryId())).build());
        }
        return builder.build();
    }

    public static Task of(TaskEntity entity) {
        return Task.builder()
                .id(entity.getId().toString())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .completed(entity.getCompleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .userId(entity.getUser() != null ? entity.getUser().getId().toString() : null)
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId().toString() : null)
                .build();
    }

    public static Task of(TaskRequest request) {
        return Task.builder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(request.getCompleted())
                .categoryId(request.getCategoryId())
                .build();
    }
}
