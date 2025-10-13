package com.money.manager.money_manager_api.controller;

import com.money.manager.money_manager_api.dto.CategoryDTO;
import com.money.manager.money_manager_api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategoriesForCurrentUser() {
        List<CategoryDTO> categories = categoryService.getCategoriesForCurrentUser();
        return ResponseEntity.ok(categories);
    }

    // --- THIS IS THE NEW ENDPOINT TO FIX THE ERROR ---
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByType(@PathVariable String type) {
        // We convert to uppercase to ensure consistency (e.g., "income" becomes "INCOME")
        List<CategoryDTO> categories = categoryService.getCategoriesForCurrentUserAndType(type.toUpperCase());
        return ResponseEntity.ok(categories);
    }
}