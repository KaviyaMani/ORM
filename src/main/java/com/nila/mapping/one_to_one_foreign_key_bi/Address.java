package com.nila.mapping.one_to_one_foreign_key_bi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street;
    private String city;

    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
    @JsonIgnore
    private User user;

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }
}
