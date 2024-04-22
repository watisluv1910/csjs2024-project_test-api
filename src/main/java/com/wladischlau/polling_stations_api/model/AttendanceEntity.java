package com.wladischlau.polling_stations_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wladischlau.polling_stations_api.model.station.StationEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "attendance")
public class AttendanceEntity {

    @Nullable
    @Id
    @SequenceGenerator(name = "attendance_id_seq",
            sequenceName = "attendance_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "attendance_id_seq")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /**
     * Hour of day in 24-hours notation.
     */
    @NotNull
    @PositiveOrZero
    @Max(23)
    @Column(name = "hour", nullable = false)
    private Integer hour;

    @NotNull
    @Positive
    @Column(name = "attendance", nullable = false)
    private Long attendance;

    @NotNull
    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "polling_station_id", referencedColumnName = "id")
    private StationEntity pollingStation;

    public AttendanceEntity(
            LocalDate date,
            Integer hour,
            Long attendance,
            StationEntity pollingStation
    ) {
        this.date = date;
        this.hour = hour;
        this.attendance = attendance;
        this.pollingStation = pollingStation;
    }
}
