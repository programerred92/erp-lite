package com.magicbox.erp_lite.customer;

import com.magicbox.erp_lite.domain.customer.CustomerInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CustomerInfo Domain Test")
class CustomerInfoTest {

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when id is null")
        void shouldThrowWhenIdIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new CustomerInfo(null, "John Doe", "john@example.com", "123", "Street 1",
                            "City", "00000", "Acme"));

            assertEquals("id is null", exception.getMessage());
        }

        @ParameterizedTest(name = "name=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " "})
        @DisplayName("Should throw IllegalArgumentException when name is null or blank")
        void shouldThrowWhenNameIsNullOrBlank(String invalidName) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new CustomerInfo(1L, invalidName, "john@example.com", "123", "Street 1",
                            "City", "00000", "Acme"));

            assertEquals("name is null", exception.getMessage());
        }

        @Test
        @DisplayName("Should create a CustomerInfo with all provided attributes correctly set")
        void shouldCreateCustomerInfoWithValidData() {
            CustomerInfo customerInfo = new CustomerInfo(1L, "John Doe", "john@example.com", "123",
                    "Street 1", "City", "00000", "Acme");

            assertEquals(1L, customerInfo.id());
            assertEquals("John Doe", customerInfo.name());
            assertEquals("john@example.com", customerInfo.email());
            assertEquals("123", customerInfo.phone());
            assertEquals("Street 1", customerInfo.address());
            assertEquals("City", customerInfo.city());
            assertEquals("00000", customerInfo.zipcode());
            assertEquals("Acme", customerInfo.companyName());
        }
    }
}
