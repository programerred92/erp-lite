package com.magicbox.erp_lite.rest.adapters;

import com.magicbox.erp_lite.domain.customer.CustomerInfo;
import com.magicbox.erp_lite.domain.customer.CustomerProviderService;
import com.magicbox.erp_lite.rest.mappers.CustomerMapper;
import com.magicbox.erp_lite.rest.dtos.UserDTO;
import com.magicbox.erp_lite.rest.models.JsonPlaceholderConfigModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Service
@Slf4j
public class JsonPLaceholderCustomerProviderAdapter implements CustomerProviderService {

    private final RestClient jsonClient;
    private final CustomerMapper customerMapper;
    private final String endpoint;

    public JsonPLaceholderCustomerProviderAdapter(
            @Qualifier("jsonplaceholder") RestClient restClient,
            CustomerMapper customerMapper,
            JsonPlaceholderConfigModel jsonConfig) {
        this.jsonClient = restClient;
        this.customerMapper = customerMapper;
        this.endpoint = jsonConfig.usersEndpoints();
    }

    @Override
    public Optional<CustomerInfo> findById(Long id) {
        log.info("Checking if customer exists with ID: {}", id);
        try{
            final UserDTO response = this.jsonClient.get()
                    .uri(endpoint, id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res)->{
                        log.error("Customer with ID: {} not found", id);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res)->{
                        log.error("Server error while trying to find customer with ID: {}", id);
                    })
                    .body(UserDTO.class);

            if(response == null){
                log.warn("Customer with ID: {} not found", id);
                return Optional.empty();
            }

            log.info("Customer with ID: {} found", id);
            return Optional.of(this.customerMapper.toCustomerInfo(response));

        } catch (RestClientException rce) {
            log.error("Error while trying to find customer with ID: {}", id, rce);
            return Optional.empty();
        }catch (Exception e){
            log.error("Unexpected error while trying to find customer with ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(Long id) {
        log.info("Checking if customer exists with ID: {}", id);
        return false;
    }
}
