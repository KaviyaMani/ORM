package com.nila.mapping.validation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Address {

    @Pattern(regexp = "^[0-9]{5}$", message = "Postal code must be a 5-digit number.")
    private String postalCode;

    @NotNull(message = "Street name should not be null")
    private String street;

    @NotNull(message = "City name should not be null")
    @Size(min = 2, max = 50)
    private String city;
}
