package com.magicbox.erp_lite.rest.mappers;

import com.magicbox.erp_lite.domain.customer.CustomerInfo;
import com.magicbox.erp_lite.rest.dtos.AddressDTO;
import com.magicbox.erp_lite.rest.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CustomerMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")

    @Mapping(target = "address", source = "address", qualifiedByName = "mapAddress")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "zipcode", source = "address.zipcode")

    @Mapping(target = "companyName", source = "company.name")
    CustomerInfo toCustomerInfo(UserDTO user);

    @Named("mapAddress")
    default String mapAddress(AddressDTO address) {
        if (address == null) {
            return null;
        }
        String street = address.street();
        String suite = address.suite();

        if (suite == null || suite.isBlank()) {
            return street;
        }

        if (street == null || street.isBlank()) {
            return suite;
        }
        return street + ", " + suite;
        }
    }
