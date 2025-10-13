package com.money.manager.money_manager_api.controller;

import com.money.manager.money_manager_api.service.ExcelService;
import com.money.manager.money_manager_api.service.ExpenseService;
import com.money.manager.money_manager_api.service.IncomeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @GetMapping("/incomes/excel")
    public void exportIncomesToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=incomes_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        excelService.writeIncomesToExcel(response.getOutputStream(), incomeService.getIncomesForCurrentUser());
    }

    @GetMapping("/expenses/excel")
    public void exportExpensesToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=expenses_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        excelService.writeExpensesToExcel(response.getOutputStream(), expenseService.getExpensesForCurrentUser());
    }
}