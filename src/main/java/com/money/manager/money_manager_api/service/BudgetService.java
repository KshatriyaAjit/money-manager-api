package com.money.manager.money_manager_api.service;

import com.money.manager.money_manager_api.dto.BudgetDTO;
import com.money.manager.money_manager_api.entity.BudgetEntity;
import com.money.manager.money_manager_api.entity.CategoryEntity;
import com.money.manager.money_manager_api.entity.ProfileEntity;
import com.money.manager.money_manager_api.repository.BudgetRepository;
import com.money.manager.money_manager_api.repository.CategoryRepository;
import com.money.manager.money_manager_api.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    /**
     * Creates or updates a budget for a specific category and month for the current user.
     * @param budgetDTO The budget details from the request.
     * @return The saved budget as a DTO.
     */
    public BudgetDTO setBudget(BudgetDTO budgetDTO) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(budgetDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // Normalize the period to always be the first day of the selected month
        LocalDate period = budgetDTO.getPeriod().withDayOfMonth(1);

        BudgetEntity budgetEntity = BudgetEntity.builder()
                .amount(budgetDTO.getAmount())
                .period(period)
                .profile(currentUser)
                .category(category)
                .build();

        BudgetEntity savedBudget = budgetRepository.save(budgetEntity);
        return toDTO(savedBudget);
    }

    /**
     * Retrieves all budgets for a given month for the current user, including the amount spent.
     * @param month A date representing the month to fetch budgets for (e.g., "2025-10-01").
     * @return A list of budgets with their spending progress.
     */
    public List<BudgetDTO> getBudgetsForMonth(LocalDate month) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

        List<BudgetEntity> budgets = budgetRepository.findByProfileIdAndPeriodBetween(currentUser.getId(), startDate, endDate);

        // For each budget, calculate how much has been spent and add it to the DTO
        return budgets.stream().map(budget -> {
            BigDecimal spentAmount = expenseRepository.findTotalSpentForCategoryInPeriod(
                    currentUser.getId(),
                    budget.getCategory().getId(),
                    startDate,
                    endDate
            );
            BudgetDTO dto = toDTO(budget);
            dto.setSpentAmount(spentAmount);
            return dto;
        }).collect(Collectors.toList());
    }

    // Helper method to convert a BudgetEntity to a BudgetDTO
    private BudgetDTO toDTO(BudgetEntity entity) {
        BudgetDTO dto = new BudgetDTO();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setPeriod(entity.getPeriod());
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }
        return dto;
    }
}