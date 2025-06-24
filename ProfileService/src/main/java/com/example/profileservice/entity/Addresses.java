package com.example.profileservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "addresses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Addresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    UserProfile userProfile;

    @Column(name = "contact_name", nullable = false, length = 100)
    String contactName;

    @Column(name = "contact_phone", nullable = false, length = 15)
    String contactPhone;

    @Column(name = "address_line1", nullable = false, length = 255)
    String addressLine1;

    @Column(name = "address_line2", length = 255)
    String addressLine2;

    @Column(nullable = false, length = 100)
    String city;

    @Column(nullable = false, length = 100)
    String province;

    @Column(name = "postal_code", nullable = false, length = 20)
    String postalCode;

    @Column(nullable = false, length = 100)
    String country;

    @Column(name = "is_default", nullable = false)
    Boolean isDefault = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}