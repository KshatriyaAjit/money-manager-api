package com.money.manager.money_manager_api.repository;

import com.money.manager.money_manager_api.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

    /**
     * Finds all budgets for a specific user within a given date range.
     * This will be used to fetch all budgets for a specific month.
     * @param profileId The ID of the user's profile.
     * @param startDate The start date of the period.
     * @param endDate The end date of the period.
     * @return A list of budgets for that user in that period.
     */
    List<BudgetEntity> findByProfileIdAndPeriodBetween(Long profileId, LocalDate startDate, LocalDate endDate);
}