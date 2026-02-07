package br.com.jtech.tasklist.application.core.domains;

import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    public static User of(UserEntity entity) {
        return User.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .name(getName())
                .email(getEmail())
                .password(getPassword())
                .build();
    }
}
