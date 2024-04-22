package com.wladischlau.polling_stations_api.model.station;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OpeningHoursValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpeningHoursConstraint {
    String message() default "Opens at hour must be less than closes at hour by at least one";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
