package com.money.manager.money_manager_api.repository;

import com.money.manager.money_manager_api.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    // This method finds a user by their email address
    Optional<ProfileEntity> findByEmail(String email);

    // ADD THIS NEW METHOD
    // This method will find a user by their unique activation token
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}