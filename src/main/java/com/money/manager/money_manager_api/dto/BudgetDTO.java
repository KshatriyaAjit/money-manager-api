package com.money.manager.money_manager_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BudgetDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDate period;
    private Long categoryId;
    private String categoryName;
    private BigDecimal spentAmount; // To track how much has been spent against the budget
}