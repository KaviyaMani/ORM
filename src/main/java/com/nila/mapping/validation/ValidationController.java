package com.nila.mapping.validation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
@Validated
public class ValidationController {

    @Autowired
    CustomValidatorUtility validatorUtility;

    @PostMapping("/valid")
    public ResponseEntity<String> checkValid(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok("User is valid");
    }

    @PostMapping("/valid-1")
    public ResponseEntity<String> checkValid1(@RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    bindingResult.getFieldErrors()
                            .stream()
                            .map(error-> error.getField()+ " : "+error.getDefaultMessage())
                            .collect(Collectors.joining("\n"))
            );
        }
        return ResponseEntity.ok("User is valid");
    }

    @PostMapping("/valid-2")
    public ResponseEntity<String> checkValidCustomValidator(@RequestBody UserDTO userDTO) {
        validatorUtility.validateData(userDTO);
        return ResponseEntity.ok("User is valid");
    }


    @GetMapping("/validatePathVariable/{id}")
    public String validatePathVariable(@PathVariable("id") @Min(value = 5, message = "Minimum value should be 5") int id) {
        return "Valid path variable";
    }

    @GetMapping("/validateRequestParam")
    public String validateRequestParam(@RequestParam("id") @Min(value = 5, message = "Minimum value should be 5") int id) {
        return "Valid path variable";
    }

}
