package com.money.manager.money_manager_api.service;

import com.money.manager.money_manager_api.dto.ExpenseDTO;
import com.money.manager.money_manager_api.dto.IncomeDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class ExcelService {

    public void writeIncomesToExcel(OutputStream outputStream, List<IncomeDTO> incomes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Incomes");

            // Create Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Amount", "Date", "Category"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Create Data Rows
            int rowNum = 1;
            for (IncomeDTO income : incomes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(income.getId());
                row.createCell(1).setCellValue(income.getName());
                row.createCell(2).setCellValue(income.getAmount().doubleValue());
                row.createCell(3).setCellValue(income.getDate().toString());
                row.createCell(4).setCellValue(income.getCategoryName());
            }

            workbook.write(outputStream);
        }
    }

    public void writeExpensesToExcel(OutputStream outputStream, List<ExpenseDTO> expenses) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Expenses");

            // Create Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Amount", "Date", "Category"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Create Data Rows
            int rowNum = 1;
            for (ExpenseDTO expense : expenses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(expense.getId());
                row.createCell(1).setCellValue(expense.getName());
                row.createCell(2).setCellValue(expense.getAmount().doubleValue());
                row.createCell(3).setCellValue(expense.getDate().toString());
                row.createCell(4).setCellValue(expense.getCategoryName());
            }

            workbook.write(outputStream);
        }
    }
}