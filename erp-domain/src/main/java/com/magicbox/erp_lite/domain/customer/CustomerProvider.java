package com.magicbox.erp_lite.domain.customer;

import java.util.Optional;

public interface CustomerProvider {

    Optional<CustomerInfo> findById(String code);
    boolean existsById(Long id);
}
