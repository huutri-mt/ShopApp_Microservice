package com.example.profileservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "user_profile")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserProfile {
    @Id
    int id;

    @Column(name = "full_name")
    String fullName;
    String email;
    @Column(name = "phone_number")
    String phoneNumber;
    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;
    String gender;
    @Column(name = "create_at")
    LocalDate createAt;
    @Column(name = "update_at")
    LocalDate updateAt;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    List<Addresses> addresses;


}
