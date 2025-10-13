package com.money.manager.money_manager_api.controller;

import com.money.manager.money_manager_api.dto.FilterDTO;
import com.money.manager.money_manager_api.service.ExpenseService;
import com.money.manager.money_manager_api.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<List<?>> filterTransactions(@RequestBody FilterDTO filterDTO) {
        // Handle null values for optional filter fields
        String type = filterDTO.getType();
        LocalDate startDate = filterDTO.getStartDate() != null ? filterDTO.getStartDate() : LocalDate.of(1900, 1, 1);
        LocalDate endDate = filterDTO.getEndDate() != null ? filterDTO.getEndDate() : LocalDate.now();
        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword() : "";

        if ("INCOME".equalsIgnoreCase(type)) {
            return ResponseEntity.ok(incomeService.filterIncomes(startDate, endDate, keyword));
        } else if ("EXPENSE".equalsIgnoreCase(type)) {
            return ResponseEntity.ok(expenseService.filterExpenses(startDate, endDate, keyword));
        } else {
            // Return an error if the type is invalid
            return ResponseEntity.badRequest().body(Collections.singletonList("Invalid transaction type specified."));
        }
    }
}

