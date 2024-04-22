package com.wladischlau.polling_stations_api.model.station;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OpeningHoursValidator
        implements ConstraintValidator<OpeningHoursConstraint, StationEntity> {

    @Override
    public boolean isValid(
            StationEntity pollingStationEntity,
            ConstraintValidatorContext context
    ) {
        return pollingStationEntity.getOpensAtHour() < pollingStationEntity.getClosesAtHour();
    }
}
