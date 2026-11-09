package com.magicbox.erp_lite.rest.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jsonplaceholder.api")
@Validated
public record JsonPlaceholderConfigModel(

        @NotBlank(message = "La URL base de JSONPlaceholder es obligatoria")
        String baseUrl,

        @NotBlank(message = "El endpoint de usuarios es obligatorio")
        String usersEndpoints,

        @Positive(message = "El tiempo de conexión debe ser mayor a cero")
        int connectionTimeout,

        @Positive(message = "El tiempo de lectura debe ser mayor a cero")
        int readTimeout,

        boolean enabled
) {
}
