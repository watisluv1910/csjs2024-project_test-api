package com.wladischlau.polling_stations_api.service;

import com.wladischlau.polling_stations_api.model.Address;
import com.wladischlau.polling_stations_api.model.StationAddressRelationEntity;
import com.wladischlau.polling_stations_api.model.StationAddressesList;
import com.wladischlau.polling_stations_api.model.address.AddressEntity;
import com.wladischlau.polling_stations_api.model.address.AddressType;
import com.wladischlau.polling_stations_api.model.station.StationEntity;
import com.wladischlau.polling_stations_api.repo.AddressRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    // TODO: Add exception throw
    public AddressEntity getActualAddressEntity(
            final Collection<StationAddressRelationEntity> stationAddresses
    ) {
        return stationAddresses
                .stream()
                .filter(a -> a.getAddressType() == AddressType.ADDRESS_TYPE_ACTUAL)
                .map(a -> addressRepository.findById(a.getAddress().getId()).orElseThrow())
                .findFirst()
                .orElseThrow();
    }

    // TODO: Move to address mapper
    // TODO: Add null check
    public @NotNull Address toAddressResponse(AddressEntity entity) {
        return new Address(
                entity.getId(),
                entity.getRegionCode(),
                entity.getCity(),
                entity.getStreet(),
                entity.getHouseNumber()
        ).building(entity.getBuilding());
    }

    public Long getBorderAddressIdBy(
            @NotBlank @Pattern(regexp = "[A-Z]{2,3}") String regionCode,
            @NotBlank String city,
            @NotBlank String street,
            @NotBlank String houseNumber,
            @Nullable String building
    ) {
        return addressRepository
                .findBorderAddressIdByAllParams(
                        regionCode,
                        removeUnderscore(city),
                        removeUnderscore(street),
                        houseNumber,
                        building
                );
    }

    public static String removeUnderscore(String value) {
        return value.replace("_", " ");
    }

    public @NotNull StationAddressesList getStationAddressesList(StationEntity station) {
        var borderAddresses = getStationAddressResponsesByType(
                station,
                AddressType.ADDRESS_TYPE_BORDER
        ).collect(Collectors.toSet());

        var actualAddress = getStationAddressResponsesByType(
                station,
                AddressType.ADDRESS_TYPE_ACTUAL
        ).toList().getFirst();

        var commissionAddress = getStationAddressResponsesByType(
                station,
                AddressType.ADDRESS_TYPE_COMMISSION
        ).toList().getFirst();

        return new StationAddressesList(borderAddresses, actualAddress, commissionAddress);
    }

    public @NotNull Stream<Address> getStationAddressResponsesByType(
            StationEntity station,
            AddressType type
    ) {
        return station
                .getStationAddresses()
                .stream()
                .filter(a -> a.getAddressType() == type)
                .map(stationAddressRelation -> toAddressResponse(stationAddressRelation.getAddress()));
    }
}
