package com.money.manager.money_manager_api.repository;

import com.money.manager.money_manager_api.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByProfileId(Long profileId);

    // --- ADD THIS NEW METHOD ---
    /**
     * Finds all categories for a specific user and of a specific type (e.g., "INCOME").
     * @param profileId The ID of the user's profile.
     * @param type The type of the category ("INCOME" or "EXPENSE").
     * @return A list of matching categories.
     */
    List<CategoryEntity> findByProfileIdAndType(Long profileId, String type);
}