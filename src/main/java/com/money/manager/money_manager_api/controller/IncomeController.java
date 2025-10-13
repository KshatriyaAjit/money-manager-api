package com.money.manager.money_manager_api.controller;

import com.money.manager.money_manager_api.dto.IncomeDTO;
import com.money.manager.money_manager_api.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO) {
        IncomeDTO addedIncome = incomeService.addIncome(incomeDTO);
        return new ResponseEntity<>(addedIncome, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getAllIncomesForCurrentUser() {
        List<IncomeDTO> incomes = incomeService.getIncomesForCurrentUser();
        return ResponseEntity.ok(incomes);
    }

    // --- THIS IS THE NEW ENDPOINT ---
    @PutMapping("/{id}")
    public ResponseEntity<IncomeDTO> updateIncome(@PathVariable Long id, @RequestBody IncomeDTO incomeDTO) {
        IncomeDTO updatedIncome = incomeService.updateIncome(id, incomeDTO);
        return ResponseEntity.ok(updatedIncome);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        // Returns a 204 No Content status, which is the standard for a successful DELETE
        return ResponseEntity.noContent().build();
    }
}

