package com.magicbox.erp_lite.rest.dtos;

public record AddressDTO(
        String street,
        String suite,
        String city,
        String zipcode,
        GeoDTO geo
) {
}
