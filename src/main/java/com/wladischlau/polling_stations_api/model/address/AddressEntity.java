package com.wladischlau.polling_stations_api.model.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wladischlau.polling_stations_api.model.StationAddressRelationEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "address")
public class AddressEntity {

    @Nullable
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Pattern(regexp = "[A-Z]{2,3}")
    @Column(name = "region_code", nullable = false)
    private String regionCode;

    @NotBlank
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank
    @Column(name = "street", nullable = false)
    private String street;

    @NotBlank
    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Nullable
    @Column(name = "building")
    private String building;

    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(
            mappedBy = "address",
            cascade = CascadeType.ALL
    )
    private List<StationAddressRelationEntity> stationAddresses;
}
