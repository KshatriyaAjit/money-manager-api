package com.money.manager.money_manager_api.controller;

import com.money.manager.money_manager_api.dto.BudgetDTO;
import com.money.manager.money_manager_api.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    /**
     * Endpoint to set a budget for a specific category and month.
     * @param budgetDTO The budget details from the request body.
     * @return The created or updated budget.
     */
    @PostMapping
    public ResponseEntity<BudgetDTO> setBudget(@RequestBody BudgetDTO budgetDTO) {
        BudgetDTO createdBudget = budgetService.setBudget(budgetDTO);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get all budgets for a specific month.
     * @param month A date representing the month to fetch (e.g., "2025-10-01").
     * @return A list of budgets for that month, including spending progress.
     */
    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getBudgetsForMonth(
            @RequestParam("month") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        List<BudgetDTO> budgets = budgetService.getBudgetsForMonth(month);
        return ResponseEntity.ok(budgets);
    }
}

