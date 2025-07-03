package com.example.profileservice.entity;

import com.example.profileservice.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user_profiles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Thêm auto-increment
    int id;

    @Column(name = "full_name", nullable = false, length = 100)
    String fullName;

    @Column(nullable = false, unique = true, length = 100) // Email phải là duy nhất
    String email;

    @Column(name = "phone_number", nullable = false, length = 15)
    String phoneNumber;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    Gender gender;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDate updatedAt;

    @OneToMany(
            mappedBy = "userProfile",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    List<Addresses> addresses;


    // Helper method để quản lý quan hệ hai chiều
    public void addAddress(Addresses address) {
        addresses.add(address);
        address.setUserProfile(this);
    }

    public void removeAddress(Addresses address) {
        addresses.remove(address);
        address.setUserProfile(null);
    }
}