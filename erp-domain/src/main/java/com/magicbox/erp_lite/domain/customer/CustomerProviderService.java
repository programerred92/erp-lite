package com.magicbox.erp_lite.domain.customer;

import java.util.Optional;

public interface CustomerProviderService {

    Optional<CustomerInfo> findById(Long id);
    boolean existsById(Long id);
}
