package com.nila.mapping.validation;

import jakarta.validation.*;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomValidatorUtility {

    void validateData(Object object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
