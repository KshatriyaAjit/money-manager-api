package com.money.manager.money_manager_api.service;

import com.money.manager.money_manager_api.dto.ExpenseDTO;
import com.money.manager.money_manager_api.entity.CategoryEntity;
import com.money.manager.money_manager_api.entity.ExpenseEntity;
import com.money.manager.money_manager_api.entity.ProfileEntity;
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
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        ExpenseEntity expenseEntity = toEntity(expenseDTO, currentUser, category);
        ExpenseEntity savedExpense = expenseRepository.save(expenseEntity);
        return toDTO(savedExpense);
    }

    // --- THIS IS THE NEW METHOD ---
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        ProfileEntity currentUser = profileService.getCurrentProfile();

        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense record not found"));

        // Security check: Ensure the user owns this record
        if (!expense.getProfile().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to edit this record");
        }

        // Find the new category
        CategoryEntity category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // Update the fields
        expense.setName(expenseDTO.getName());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(expenseDTO.getDate());
        expense.setCategory(category);

        ExpenseEntity updatedExpense = expenseRepository.save(expense);
        return toDTO(updatedExpense);
    }

    public List<ExpenseDTO> getExpensesForCurrentUser() {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        List<ExpenseEntity> expenses = expenseRepository.findByProfileIdOrderByDateDesc(currentUser.getId());
        return expenses.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void deleteExpense(Long id) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense record not found"));

        if (!expense.getProfile().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this record");
        }

        expenseRepository.delete(expense);
    }

    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        List<ExpenseEntity> expenses = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                currentUser.getId(),
                startDate,
                endDate,
                keyword
        );
        return expenses.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public BigDecimal getTotalExpenseForCurrentUser() {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        return expenseRepository.findTotalExpenseByProfileId(currentUser.getId());
    }

    // --- Helper Methods ---

    private ExpenseDTO toDTO(ExpenseEntity entity) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
                .name(dto.getName())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }
}

