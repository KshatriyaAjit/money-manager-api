package com.money.manager.money_manager_api.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FilterDTO {
    private String type; // "INCOME" or "EXPENSE"
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;
}
