package com.magicbox.erp_lite.persistence.aws.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "aws.s3")
@Validated
public record AwsConfigModel(

        @NotBlank(message = "El nombre del bucket S3 no puede estar vacío")
        String bucketName,

        @NotBlank(message = "La región de AWS es obligatoria")
        String region,

        @NotBlank(message = "El endpoint de S3 es obligatorio")
        String endpoint,

        @NotBlank(message = "La access key de AWS es obligatoria")
        String accessKey,

        @NotBlank(message = "La secret key de AWS es obligatoria")
        String secretKey,

        boolean pathStyleEnabled

) {
    public String getBucketUrl() {
        return String.format("%s/%s", this.endpoint, this.bucketName);
    }
}