package com.wladischlau.polling_stations_api.service;

import com.wladischlau.polling_stations_api.model.*;
import com.wladischlau.polling_stations_api.model.station.StationEntity;
import com.wladischlau.polling_stations_api.repo.StationRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class StationService {

    private final StationRepository stationRepository;
    private final AddressService addressService;
    private final AttendanceService attendanceService;

    @Transactional
    public StationInfo getStationById(Long stationId, LocalDate borderDate) {
        var station = stationRepository
                .findById(stationId)
                .orElseThrow(
                        () -> new NoSuchElementException("Station not found by id: " + stationId)
                );
        return toStationInfoResponse(station, borderDate);
    }

    @Transactional
    public StationInfo getStationByRegionCodeAndStationNumber(
            String regionCode,
            Integer stationNumber,
            LocalDate borderDate
    ) {
        var station = stationRepository
                .findByStationNumberAndRegionCode(stationNumber, regionCode)
                .orElseThrow(
                        () -> new NoSuchElementException("Station with number " +
                                                         stationNumber +
                                                         " not found in region with code: " +
                                                         regionCode
                        )
                );

        return toStationInfoResponse(station, borderDate);
    }

    @Transactional
    public StationInfo getStationByBorderAddress(
            Address borderAddress,
            LocalDate borderDate
    ) {
        var borderAddressId = addressService.getBorderAddressIdBy(
                borderAddress.getRegionCode(),
                borderAddress.getCity(),
                borderAddress.getStreet(),
                borderAddress.getHouseNumber(),
                borderAddress.getBuilding()
        );

        if (borderAddressId == null) {
            throw new NoSuchElementException(
                    "Border address not found by parameters: " + borderAddress
            );
        }

        var station = stationRepository.findByBorderAddressId(borderAddressId);

        return toStationInfoResponse(station, borderDate);
    }

    @Transactional
    public StationList getStationList() {
        var foundStations = stationRepository.findAll();
        if (foundStations.isEmpty())
            throw new NoSuchElementException("No stations found.");
        return toStationListResponse(foundStations);
    }

    @Transactional
    public StationList getStationListFiltered(
            List<@Size(min = 2, max = 3) String> regions,
            List<@Min(1) Integer> stations
    ) {
        if (regions != null && !regions.isEmpty() &&
            stations != null && !stations.isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "Only one of region-code or ps-number " +
                    "arrays should be provided.");
        }

        List<StationEntity> foundStations = List.of();

        if (regions != null) {
            foundStations = stationRepository.findAllByRegionCodes(regions);
        } else if (stations != null) {
            foundStations = stationRepository.findAllByPsNumberInOrderByPsNumber(stations);
        }

        if (foundStations.isEmpty()) {
            return new StationList(List.of());
        }

        return toStationListResponse(foundStations);
    }

    public StationInfo toStationInfoResponse(
            StationEntity station,
            LocalDate borderDate
    ) {
        return new StationInfo(
                station.getId(),
                station.getPsNumber(),
                station.getHotline(),
                station.getOpensAtHour(),
                station.getClosesAtHour(),
                addressService.getStationAddressesList(station)
        ).attendance(attendanceService.getStationAttendanceResponseList(station, borderDate));
    }

    public StationList toStationListResponse(
            List<StationEntity> pollingStations
    ) {
        return new StationList(
                convertToStationListRegionsInner(
                        pollingStations
                                .stream()
                                .map(this::toStationInfoBriefResponse)
                )
        );
    }

    // TODO: Move to stations mapper
    // TODO: Rework
    // TODO: Add exception throw
    public StationInfoBrief toStationInfoBriefResponse(
            StationEntity stationEntity
    ) {
        return new StationInfoBrief(
                stationEntity.getId(),
                stationEntity.getPsNumber(),
                addressService.toAddressResponse(
                        addressService.getActualAddressEntity(
                                stationEntity.getStationAddresses()
                        )
                )
        );
    }

    public List<StationListRegionsInner> convertToStationListRegionsInner(
            Stream<StationInfoBrief> foundStations
    ) {
        Map<String, List<StationInfoBrief>> groupedByCode = foundStations
                .collect(
                        Collectors.groupingBy(
                                station -> station
                                        .getActualAddress()
                                        .getRegionCode()
                        )
                );

        return groupedByCode
                .entrySet()
                .stream()
                .map(entry -> new StationListRegionsInner(
                                entry.getKey(),
                                entry.getValue()
                        )
                )
                .toList();
    }
}
