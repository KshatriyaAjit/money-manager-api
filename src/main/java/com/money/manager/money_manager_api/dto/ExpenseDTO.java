package com.money.manager.money_manager_api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ExpenseDTO {
    private Long id;
    private String name;
    private BigDecimal amount;
    private LocalDate date;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
}
