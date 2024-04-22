package com.wladischlau.polling_stations_api.service;

import com.wladischlau.polling_stations_api.model.Attendance;
import com.wladischlau.polling_stations_api.model.AttendanceEntity;
import com.wladischlau.polling_stations_api.model.station.StationEntity;
import com.wladischlau.polling_stations_api.repo.AttendanceRepository;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class AttendanceService {

    public static final Integer MIN_GENERATED_ATTENDANCE = 500;
    public static final Integer MAX_GENERATED_ATTENDANCE = 2500;

    public static final LocalDate ELECTIONS_START_DATE = LocalDate.of(2024, 3, 15);
    public static final LocalDate ELECTIONS_END_DATE = LocalDate.of(2024, 3, 17);
    public static final Long ELECTIONS_DURATION = Duration
            .between(
                    ELECTIONS_START_DATE.atStartOfDay(),
                    ELECTIONS_END_DATE.atStartOfDay()
            ).toDays() + 1;

    private final AttendanceRepository attendanceRepository;

    public static List<Long> generateHourlyAttendanceDistribution(
            Integer averageTotalAttendance,
            Integer totalHours
    ) {
        var hourlyAttendance = new ArrayList<Long>(totalHours);

        Random random = new Random();

        int averageHourlyAttendance = averageTotalAttendance / totalHours;

        // Generate skewed distribution with dispersion
        for (int hour = 0; hour < totalHours; hour++) {

            // Skew attendance towards afternoon and evening hours
            double skewFactor = Math.pow((hour) / (double) totalHours, 2.0);
            int expectedAttendance = (int) (averageHourlyAttendance * (1 + skewFactor));

            // Add random variation with small standard deviation
            double randomVariation = random.nextGaussian() * 10;
            long finalAttendance = Math.max(0, (long) (expectedAttendance + randomVariation));

            hourlyAttendance.add(finalAttendance);
        }

        return hourlyAttendance;
    }

    public List<Attendance> getStationAttendanceResponseList(
            StationEntity station,
            LocalDate borderDate
    ) {
        List<AttendanceEntity> stationAttendanceList =
                attendanceRepository.findAllByPollingStationId(station.getId());

        if (borderDate.isBefore(ELECTIONS_START_DATE)) {
            return List.of();
        }

        if (stationAttendanceList.isEmpty()) {
            for (int i = 0; i < ELECTIONS_DURATION; i++) {
                saveHourlyAttendanceForDay(
                        stationAttendanceList,
                        station,
                        ELECTIONS_START_DATE.plusDays(i)
                );
            }
        }

        if (borderDate.isBefore(ELECTIONS_END_DATE)) {
            stationAttendanceList = stationAttendanceList
                    .stream()
                    .filter(a -> !a.getDate().isAfter(borderDate))
                    .toList();
        }

        return stationAttendanceList.stream().map(this::toAttendanceResponse).toList();
    }

    private void saveHourlyAttendanceForDay(
            List<AttendanceEntity> stationAttendanceList,
            StationEntity station,
            LocalDate attendanceDate
    ) {
        Random random = new Random();
        Integer averageTotalAttendance =
                random.nextInt(
                        MAX_GENERATED_ATTENDANCE - MIN_GENERATED_ATTENDANCE
                ) + MIN_GENERATED_ATTENDANCE;

        var hourlyAttendance = generateHourlyAttendanceDistribution(
                averageTotalAttendance,
                station.getClosesAtHour() - station.getOpensAtHour()
        );

        for (int currHour = station.getOpensAtHour();
             currHour < station.getClosesAtHour();
             currHour++
        ) {
            stationAttendanceList.add(
                    attendanceRepository.save(
                            new AttendanceEntity(
                                    attendanceDate,
                                    currHour,
                                    hourlyAttendance.get(
                                            currHour - station.getOpensAtHour()
                                    ),
                                    station
                            )
                    )
            );
        }
    }

    public Attendance toAttendanceResponse(
            AttendanceEntity attendanceEntity
    ) {
        return new Attendance(
                attendanceEntity.getId(),
                attendanceEntity.getDate(),
                attendanceEntity.getHour(),
                attendanceEntity.getAttendance()
        );
    }
}
