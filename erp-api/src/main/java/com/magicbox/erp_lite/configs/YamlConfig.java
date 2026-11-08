package com.magicbox.erp_lite.configs;

import com.magicbox.erp_lite.persistence.aws.models.AwsConfigModel;
import com.magicbox.erp_lite.rest.models.JsonPlaceholderConfigModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties({AwsConfigModel.class, JsonPlaceholderConfigModel.class})
@PropertySource(value="classpath:aws/aws.yml", factory=YamlPropertySourceFactory.class)
@PropertySource(value="classpath:jsonplaceholder/jsonplaceholder.yml", factory=YamlPropertySourceFactory.class)
public class YamlConfig {

}
