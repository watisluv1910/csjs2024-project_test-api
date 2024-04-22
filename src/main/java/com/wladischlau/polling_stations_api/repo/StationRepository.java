package com.wladischlau.polling_stations_api.repo;

import com.wladischlau.polling_stations_api.model.station.StationEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<@Valid StationEntity, Long> {

    @Query(value = """
            SELECT ps.* FROM polling_station ps
            JOIN station_addresses sa ON ps.id = sa.polling_station_id
            JOIN address a ON sa.address_id = a.id
            WHERE sa.address_type = 'ADDRESS_TYPE_BORDER'
            AND sa.address_id = :addressId
            LIMIT 1
            """, nativeQuery = true)
    StationEntity findByBorderAddressId(
            @NotNull @Positive Long addressId
    );

    List<StationEntity> findAllByPsNumberInOrderByPsNumber(
            @NotNull List<@Positive Integer> stationNumbers
    );

    @Query(value = """
            SELECT ps.* FROM polling_station ps
            JOIN station_addresses sa ON ps.id = sa.polling_station_id
            JOIN address a ON sa.address_id = a.id
            WHERE ps.ps_number = :stationNumber
            AND a.region_code = :regionCode
            AND sa.address_type = 'ADDRESS_TYPE_ACTUAL'
            """, nativeQuery = true)
    Optional<StationEntity> findByStationNumberAndRegionCode(
            @NotNull @Positive Integer stationNumber,
            @NotBlank @Pattern(regexp = "[A-Z]{2,3}") String regionCode
    );

    @Query(value = """
            SELECT ps.* FROM polling_station ps
            JOIN station_addresses sa ON ps.id = sa.polling_station_id
            JOIN address a ON sa.address_id = a.id
            WHERE a.region_code IN (:regionCodes)
            AND sa.address_type = 'ADDRESS_TYPE_ACTUAL'
            ORDER BY a.region_code, ps.ps_number
            """, nativeQuery = true)
    List<StationEntity> findAllByRegionCodes(
            @Param("regionCodes") List<@Size(min = 2, max = 3) @NotNull String> regionCodes
    );
}
