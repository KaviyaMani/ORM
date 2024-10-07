## Jakarta Bean Validation
    Jakarta Bean Validation is a Java specification which
        lets you express constraints on object models via annotations
        lets you write custom constraints in an extensible way
        provides the APIs to validate objects and object graphs
        provides the APIs to validate parameters and return values of methods and constructors
        reports the set of violations (localized)
    JBV - standard for implementing validation logic in the Java ecosystem.
    Hibernate Validator is the standard for performing validation in Spring Boot application

#### JSR 380
    JSR 380 is a specification of the Java API for bean validation, part of Jakarta EE and JavaSE. 
    It ensures that the properties of a bean meet specific criteria, using annotations such as @NotNull, @Min, and @Max. 
    This version requires Java 17 or higher because it uses Spring Boot 3.
### Hibernate validator
    Express validation rules in a standardized way using annotation-based constraints
    offers validation annotations for Spring Boot that can be applied to the data fields within your Entity class
    allows you to follow specific rules and conditions for fields, applying validators to meet your custom constraints
    These annotations help ensure that the data meets all the conditions applied using the validators.
    Internally Hibernate validator uses default **JRS-380** implementation to validate upon the argument.

    @NotNull: Ensures a field is not null.
    @NotBlank: Enforces non-nullity and requires at least one non-whitespace character.
    @NotEmpty: Guarantees that collections or arrays are not empty.
    @Min(value): Checks if a numeric field is greater than or equal to the specified minimum value.
    @Max(value): Checks if a numeric field is less than or equal to the specified maximum value.
    @Size(min, max): Validates if a string or collection size is within a specific range.
    @Pattern(regex): Verifies if a field matches the provided regular expression.
    @Email: Ensures a field contains a valid email address format.
    @Digits(integer, fraction): Validates that a numeric field has a specified number of integer and fraction digits.
    @Past and @Future : Checks that a date or time field is in the past and future respectively.
    @AssertTrue and @AssertFalse: Ensures that a boolean field is true. and false respectively.
    @CreditCardNumber: Validates that a field contains a valid credit card number.
    @Valid: Triggers validation of nested objects or properties.
    @Validated: Specifies validation groups to be applied at the class or method level.

#### @Valid
    when applies to mathod parameter, automatically triggers validation for that parameter 
    It is placed before the object to indicate that it should be validated against specified validation rules
        @PostMapping("/create")
        ResponseEntity<String> createUser(@RequestBody @Valid User userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { // handle errors }
    If data fails validation, generates validation error message assosiate with appropriate fields.
    Validation errors captured in **BindingResult** object, which we can access to analyze and handle failures
#### @Validated
    to facilitate validation groups, to apply validation rules to specific groups of fields within a bean
    @Valid - which validates the entire bean object
    @Validated allows you to specify which validation groups to apply during the validation process
#### Validation for nested properties
    For complex objects use @Valid
    public class User {
    @NotNull private String name;
    @Valid private Address address; }

    public class ShippingAddress {
    @NotNull private String street;
    @NotNull @Size(min = 2, max = 50)
    private String city;
    @NotNull private String zipCode; }

####
    1. Validating request body
        ResponseEntity<String> validateBody(@Valid @RequestBody Input input)
        throws - MethodArgumentNotValidException
    2. Validating path variable - 
        1. Add @Validated at class level
        2. @GetMapping("/validatePathVariable/{id}")
           ResponseEntity<String> validatePathVariable(@PathVariable("id") @Min(5) int id)
        3. throws ConstraintViolationException, there is no handler by default gives 500 error
        4. @ExceptionHandler(ConstraintViolationException.class)
    3. Validating Request Param - works same as path variable
    4. Validating input of service method
        1. Add @Validated at class level
        2. Add @Valid at parameter level: void validateInput(@Valid Input input){

#### Exception Handling for validation errors
    1. Global Exception Handler - 
        @RestControllerAdvice, @ExceptionHandler(MethodArgumentNotValidException.class)
            MethodArgumentNotValidException.class is the exception class to be handled
            Error details will be available at BindingResult object
            By exception.getBindingResult().getAllErrors(), we can get hold of list of errors
            we can use them to return valid error message to userDTO
    2. In controller using bindingResult.hasErrors()
        bindingResult.getFieldErrors()
            map Field errors with error.getField() & error.getDefaultMessage()
            return bad request with error message

#### Custom Validator
    create Custom validator utility like CustomValidatorUtility.java
    call using validatorUtility.validateData(userDTO);

Refer: ValidationController.java
    