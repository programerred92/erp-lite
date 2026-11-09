package com.magicbox.erp_lite.rest.configs;

import com.magicbox.erp_lite.rest.models.JsonPlaceholderConfigModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RestClientConfig {

    private final JsonPlaceholderConfigModel jsonConfig;

    @Bean(name = "jsonplaceholder")
    @ConditionalOnProperty(
            prefix = "jsonplaceholder",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public RestClient restTemplate() {
        return RestClient.builder()
                .baseUrl(jsonConfig.baseUrl())
                .requestInterceptors( interceptor ->{
                    interceptor.add(loggingInterceptor());
                    interceptor.add(errorLoggingInterceptor());
                })
                .defaultHeaders(headers -> {
                    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .build();
    }
    private ClientHttpRequestInterceptor loggingInterceptor(){
        return (req, body, exe) ->{
            log.info("Calling jsonplaceholder API");
            log.info("Request URL: {}", req.getURI());
            log.info("Request Method: {}", req.getMethod());
            log.info("Request Headers: {}", req.getHeaders());
            log.info("Request Body: {}", new String(body));

            final long start = System.currentTimeMillis();
            final var response= exe.execute(req, body);
            try{
                final long elapsedTime = System.currentTimeMillis() - start;
                log.info("Executed in {} ms", elapsedTime);
                log.info("Response Status Code: {}", response.getStatusCode());
            } catch (Exception e) {
                log.error("Error executing request:{}", e.getMessage());
                throw e;
            }
        return response;
        };
    }


    private ClientHttpRequestInterceptor errorLoggingInterceptor(){
        return (req, body, exe) ->{
            try{
                return exe.execute(req, body);
            }catch (Exception ex){
                log.error("Error message:{}",ex.getMessage());
                throw ex;
            }
        };
    }
}
