package com.wladischlau.polling_stations_api.repo;

import com.wladischlau.polling_stations_api.model.AttendanceEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository extends BaseRepository<@Valid AttendanceEntity, Long> {

    @Query(value = """
           SELECT * FROM attendance a
           WHERE a.polling_station_id = :pollingStationId
           ORDER BY a.date, a.hour
           """, nativeQuery = true)
    List<AttendanceEntity> findAllByPollingStationId(
            @NotNull @Positive Long pollingStationId
    );
}
