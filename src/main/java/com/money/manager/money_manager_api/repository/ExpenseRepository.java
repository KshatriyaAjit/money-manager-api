package com.money.manager.money_manager_api.repository;

import com.money.manager.money_manager_api.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId, LocalDate startDate, LocalDate endDate, String name);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    List<ExpenseEntity> findByProfileIdAndDate(Long profileId, LocalDate date);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e WHERE e.profile.id = :profileId AND e.category.id = :categoryId AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal findTotalSpentForCategoryInPeriod(
            @Param("profileId") Long profileId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // --- ADD THIS NEW METHOD ---
    /**
     * Groups all expense records by month and calculates the sum of amounts for each month.
     * It returns this data directly into a list of MonthlySummaryDTOs.
     * @param profileId The ID of the user's profile.
     * @return A list of monthly expense summaries.
     */

}

