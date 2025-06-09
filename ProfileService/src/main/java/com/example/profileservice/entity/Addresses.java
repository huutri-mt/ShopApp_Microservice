package com.example.profileservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "addresses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Addresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserProfile userProfile;

    @Column(name = "contact_name")
    String contactName;

    @Column(name ="contact_phone")
    String contactPhone;

    @Column(name = "address_line1")
    String addressLine1;

    @Column(name = "address_line2")
    String addressLine2;

    String city;
    String province;

    @Column(name = "postal_code")
    String postalCode;

    String country;

    @Column(name = "is_default")
    Boolean isDefault;

}
