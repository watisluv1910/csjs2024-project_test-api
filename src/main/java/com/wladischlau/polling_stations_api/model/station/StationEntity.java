package com.wladischlau.polling_stations_api.model.station;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wladischlau.polling_stations_api.model.AttendanceEntity;
import com.wladischlau.polling_stations_api.model.StationAddressRelationEntity;
import com.wladischlau.polling_stations_api.model.address.AddressEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "polling_station")
@OpeningHoursConstraint
public class StationEntity {

    @Nullable
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Positive
    @Column(name = "ps_number", nullable = false)
    private Integer psNumber;

    @NotBlank
    @Pattern(regexp = "8-\\d{3}-\\d{3}-\\d{2}-\\d{2}")
    @Column(name = "hotline", nullable = false)
    private String hotline;

    @NotNull
    @PositiveOrZero
    @Max(23)
    @Column(name = "opens_at_hour", nullable = false)
    private Integer opensAtHour;

    @NotNull
    @PositiveOrZero
    @Max(23)
    @Column(name = "closes_at_hour", nullable = false)
    private Integer closesAtHour;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(
            mappedBy = "pollingStation",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<StationAddressRelationEntity> stationAddresses;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(
            mappedBy = "pollingStation",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<AttendanceEntity> attendances;
}
