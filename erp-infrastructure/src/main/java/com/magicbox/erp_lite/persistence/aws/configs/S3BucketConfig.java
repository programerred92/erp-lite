package com.magicbox.erp_lite.persistence.aws.configs;

import com.magicbox.erp_lite.persistence.aws.models.AwsConfigModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class S3BucketConfig {

    private final AwsConfigModel awsConfigModel;

    @Bean
    public S3Client s3Client(){
        log.info("Creating AWS S3 Client");

        var credentials = AwsBasicCredentials.create(awsConfigModel.accessKey(), awsConfigModel.secretKey());

        var s3Config = S3Configuration.builder()
                .pathStyleAccessEnabled(awsConfigModel.pathStyleEnabled())
                .build();

        return S3Client.builder()
                .region(Region.of(awsConfigModel.region()))
                .credentialsProvider(() -> credentials)
                .serviceConfiguration(s3Config)
                .build();
    }
}
