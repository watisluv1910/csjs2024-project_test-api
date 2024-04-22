package com.wladischlau.polling_stations_api.controller;

import com.wladischlau.polling_stations_api.BriefStationInfoApi;
import com.wladischlau.polling_stations_api.BriefStationInfoApiDelegate;
import com.wladischlau.polling_stations_api.model.StationList;
import com.wladischlau.polling_stations_api.service.StationService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BriefStationInfoDelegateImpl implements BriefStationInfoApiDelegate {

    private final StationService stationService;

    /**
     * GET /v1/stations : Search for brief info about all polling stations in all regions.
     *
     * @return Success (status code 200)
     * or Bad Request (status code 400)
     * @see BriefStationInfoApi#getStations
     */
    @Override
    public ResponseEntity<StationList> getStations() {
        return ResponseEntity.ok().body(stationService.getStationList());
    }

    /**
     * GET /v1/stations/filtered : Search for all polling stations, divided by page with limit, and filter by region code(s) or station number(s).
     *
     * @param regions  List of region codes to be filtered by. (optional)
     * @param stations List of station numbers to be filtered by. (optional)
     * @return Success (status code 200)
     * or Bad Request (status code 400)
     * @see BriefStationInfoApi#getStationsFiltered
     */
    @Override
    public ResponseEntity<StationList> getStationsFiltered(
            List<@Size(min = 2, max = 3) String> regions,
            List<@Min(1) Integer> stations
    ) {
        return ResponseEntity.ok().body(stationService.getStationListFiltered(regions, stations));
    }
}
