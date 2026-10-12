package com.magicbox.erp_lite.order;

import com.magicbox.erp_lite.domain.order.Customer;
import com.magicbox.erp_lite.domain.shared.CustomerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Customer Domain Test")
class CustomerTest {

    private static final CustomerId VALID_CUSTOMER_ID = CustomerId.of(1L);

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorValidations {

        @Test
        @DisplayName("Should throw IllegalArgumentException when customerId is null")
        void shouldThrowWhenCustomerIdIsNull() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Customer(null, "John Doe"));

            assertEquals("customerId must not be null", exception.getMessage());
        }

        @ParameterizedTest(name = "customerName=[{0}] should throw IllegalArgumentException")
        @NullSource
        @ValueSource(strings = {"", " "})
        @DisplayName("Should throw IllegalArgumentException when customerName is null or blank")
        void shouldThrowWhenCustomerNameIsNullOrBlank(String invalidName) {
            assertThrows(IllegalArgumentException.class, () -> new Customer(VALID_CUSTOMER_ID, invalidName));
        }

        @Test
        @DisplayName("Should create a Customer with the provided attributes")
        void shouldCreateCustomerWithValidData() {
            Customer customer = new Customer(VALID_CUSTOMER_ID, "John Doe");

            assertEquals(VALID_CUSTOMER_ID, customer.customerId());
            assertEquals("John Doe", customer.customerName());
        }
    }

    @Nested
    @DisplayName("of(customerId, customerName)")
    class OfFactory {

        @Test
        @DisplayName("Should create a Customer using the static factory method")
        void shouldCreateCustomerUsingFactory() {
            Customer customer = Customer.of(VALID_CUSTOMER_ID, "Jane Doe");

            assertEquals(VALID_CUSTOMER_ID, customer.customerId());
            assertEquals("Jane Doe", customer.customerName());
        }
    }
}
