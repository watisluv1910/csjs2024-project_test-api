package com.wladischlau.polling_stations_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wladischlau.polling_stations_api.model.address.AddressEntity;
import com.wladischlau.polling_stations_api.model.address.AddressType;
import com.wladischlau.polling_stations_api.model.station.StationEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "station_addresses")
public class StationAddressRelationEntity {

    @Nullable
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    private AddressType addressType;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "polling_station_id", referencedColumnName = "id")
    private StationEntity pollingStation;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity address;
}
