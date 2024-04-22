package com.wladischlau.polling_stations_api.controller;

import com.wladischlau.polling_stations_api.FullStationInfoApi;
import com.wladischlau.polling_stations_api.FullStationInfoApiDelegate;
import com.wladischlau.polling_stations_api.model.Address;
import com.wladischlau.polling_stations_api.model.StationInfo;
import com.wladischlau.polling_stations_api.model.StationList;
import com.wladischlau.polling_stations_api.service.StationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class FullStationInfoDelegateImpl implements FullStationInfoApiDelegate {

    private final StationService stationService;

    /**
     * GET /v1/stations/{stationId} : Search for a polling station by polling station ID.
     *
     * @param stationId Polling station unique ID. (required)
     * @param date      Date ISO 8601 format by which the attendance info should be given (exclusive). (optional)
     * @return Success (status code 200)
     * or Bad Request (status code 400)
     * @see FullStationInfoApi#getStationInfoById
     */
    @Override
    public ResponseEntity<StationInfo> getStationInfoById(Long stationId, LocalDate date) {
        return ResponseEntity
                .ok()
                .body(
                        stationService.getStationById(stationId, date)
                );
    }

    /**
     * GET /v1/stations/{stationNumber}/regions/{regionCode} : Search for a polling station by region code and polling station&#39;s number.
     *
     * @param regionCode    Region code in ISO_3166-2:RU notation. (required)
     * @param stationNumber Polling station number, unique in each region. (required)
     * @param date          Date ISO 8601 format by which the attendance info should be given (exclusive). (optional)
     * @return Success (status code 200)
     * or Bad Request (status code 400)
     * @see FullStationInfoApi#getStationInfoByRegionCodeAndStationNumber
     */
    @Override
    public ResponseEntity<StationInfo> getStationInfoByRegionCodeAndStationNumber(
            String regionCode,
            Integer stationNumber,
            LocalDate date
    ) {
        return ResponseEntity
                .ok()
                .body(
                        stationService.getStationByRegionCodeAndStationNumber(
                                regionCode,
                                stationNumber,
                                date
                        )
                );
    }

    /**
     * GET /v1/stations/byBorderAddress : Search for polling station by the full border address.
     *
     * @param borderAddress Full border address to search for. (required)
     * @param date          Date ISO 8601 format by which the attendance info should be given (exclusive). (optional)
     * @return Success (status code 200)
     * or Bad Request (status code 400)
     * @see FullStationInfoApi#getStationInfoByBorderAddress
     */
    @Override
    public ResponseEntity<StationInfo> getStationInfoByBorderAddress(Address borderAddress, LocalDate date) {
        return ResponseEntity
                .ok()
                .body(
                        stationService.getStationByBorderAddress(borderAddress, date)
                );
    }
}
