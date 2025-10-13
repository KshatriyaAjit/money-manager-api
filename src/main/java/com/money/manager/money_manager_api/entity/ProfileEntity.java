package com.money.manager.money_manager_api.entity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;
    private String profileImageUrl;
    @CreationTimestamp
    @Column(updatable = false) // This ensures createdAt is not changed on update
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private  LocalDateTime updatedAt;
    private Boolean isActive;
    private String activationToken;

    @PrePersist
    public void prePersist() {
        if(this.isActive == null) {
            isActive = false;
        }
    }


    // We will add more fields like timestamps later
}