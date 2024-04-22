package com.wladischlau.polling_stations_api.repo;

import com.wladischlau.polling_stations_api.model.address.AddressEntity;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends BaseRepository<@Valid AddressEntity, Long> {

    @Query(value = """
            SELECT a.id FROM address a
                JOIN station_addresses s ON s.address_id = a.id
            WHERE s.address_type = 'ADDRESS_TYPE_BORDER'
            AND a.region_code = :regionCode
            AND a.city = :city
            AND a.street = :street
            AND a.house_number = :houseNumber
            AND a.building = :building
            """, nativeQuery = true)
    Long findBorderAddressIdByAllParams(
            @NotBlank @Pattern(regexp = "[A-Z]{2,3}") String regionCode,
            @NotBlank String city,
            @NotBlank String street,
            @NotBlank String houseNumber,
            @Nullable String building
    );
}
