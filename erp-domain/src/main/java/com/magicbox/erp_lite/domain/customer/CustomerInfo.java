package com.magicbox.erp_lite.domain.customer;

public record CustomerInfo (
        Long id,
        String name,
        String email,
        String phone,
        String address,
        String city,
        String zipcode,
        String companyName
){
    public CustomerInfo{
        if (id == null){
            throw new IllegalArgumentException("id is null");
        }

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("name is null");
        }
    }
}
