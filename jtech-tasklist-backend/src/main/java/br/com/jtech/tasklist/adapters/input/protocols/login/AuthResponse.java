package br.com.jtech.tasklist.adapters.input.protocols.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse implements Serializable {

    private String token;
    private String refreshToken;
    @Builder.Default
    private String type = "Bearer";
}
