package com.magicbox.erp_lite.rest.dtos;

public record UserDTO(
        Long id,
        String name,
        String username,
        String email,
        AddressDTO address,
        String phone,
        String website,
        CompanyDTO company
) {
}
