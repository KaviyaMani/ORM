package com.nila.mapping.one_to_one_foreign_key_bi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uname;

    @OneToOne(fetch = FetchType.EAGER, cascade =  CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")

    private Address address;

    public User(String uname) {
        this.uname = uname;
    }
}
