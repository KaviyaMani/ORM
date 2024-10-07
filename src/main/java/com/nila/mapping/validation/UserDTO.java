package com.nila.mapping.validation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {

    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    @Min(value = 18, message = "Minimum age should be 18")
    @Max(value = 100, message = "Maximum age should be 100")
    private int age;

    @Size(min = 12, max = 12, message = "Aadhaar number should be exactly 12")
    private String aadhaarNo;

    @Valid
    private Address address;
}
