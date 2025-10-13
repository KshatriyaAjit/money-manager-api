package com.money.manager.money_manager_api.service;

import com.money.manager.money_manager_api.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity currentUser = profileService.getCurrentProfile();

        // Fetch totals
        BigDecimal totalIncome = incomeService.getTotalIncomeForCurrentUser();
        BigDecimal totalExpense = expenseService.getTotalExpenseForCurrentUser();
        BigDecimal totalBalance = totalIncome.subtract(totalExpense);

        // Fetch recent transactions
        Object recentIncomes = incomeService.getIncomesForCurrentUser();
        Object recentExpenses = expenseService.getExpensesForCurrentUser();




        // Build the response map
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalBalance", totalBalance);
        dashboardData.put("totalIncome", totalIncome);
        dashboardData.put("totalExpense", totalExpense);
        dashboardData.put("recentFiveIncome", recentIncomes);
        dashboardData.put("recentFiveExpenses", recentExpenses);

        return dashboardData;
    }
}

