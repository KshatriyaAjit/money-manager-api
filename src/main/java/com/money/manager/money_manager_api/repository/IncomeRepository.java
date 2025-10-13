package com.money.manager.money_manager_api.repository;

import com.money.manager.money_manager_api.entity.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {

    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId, LocalDate startDate, LocalDate endDate, String name);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM IncomeEntity i WHERE i.profile.id = :profileId")
    BigDecimal findTotalIncomeByProfileId(@Param("profileId") Long profileId);

    // --- ADD THIS NEW METHOD ---
    /**
     * Groups all income records by month and calculates the sum of amounts for each month.
     * It returns this data directly into a list of MonthlySummaryDTOs.
     * @param profileId The ID of the user's profile.
     * @return A list of monthly income summaries.
     */


}