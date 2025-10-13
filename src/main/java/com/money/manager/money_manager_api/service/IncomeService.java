package com.money.manager.money_manager_api.service;

import com.money.manager.money_manager_api.dto.IncomeDTO;
import com.money.manager.money_manager_api.entity.CategoryEntity;
import com.money.manager.money_manager_api.entity.IncomeEntity;
import com.money.manager.money_manager_api.entity.ProfileEntity;
import com.money.manager.money_manager_api.repository.CategoryRepository;
import com.money.manager.money_manager_api.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    public IncomeDTO addIncome(IncomeDTO incomeDTO) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(incomeDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        IncomeEntity incomeEntity = toEntity(incomeDTO, currentUser, category);
        IncomeEntity savedIncome = incomeRepository.save(incomeEntity);
        return toDTO(savedIncome);
    }

    public IncomeDTO updateIncome(Long id, IncomeDTO incomeDTO) {
        ProfileEntity currentUser = profileService.getCurrentProfile();

        IncomeEntity income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Income record not found"));

        // Security check: Ensure the user owns this record
        if (!income.getProfile().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to edit this record");
        }

        // Find the new category
        CategoryEntity category = categoryRepository.findById(incomeDTO.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // Update the fields
        income.setName(incomeDTO.getName());
        income.setAmount(incomeDTO.getAmount());
        income.setDate(incomeDTO.getDate());
        income.setCategory(category);

        IncomeEntity updatedIncome = incomeRepository.save(income);
        return toDTO(updatedIncome);
    }

    public List<IncomeDTO> getIncomesForCurrentUser() {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        List<IncomeEntity> incomes = incomeRepository.findByProfileIdOrderByDateDesc(currentUser.getId());
        return incomes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void deleteIncome(Long id) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        IncomeEntity income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Income record not found"));

        if (!income.getProfile().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this record");
        }

        incomeRepository.delete(income);
    }

    public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        List<IncomeEntity> incomes = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                currentUser.getId(),
                startDate,
                endDate,
                keyword
        );
        return incomes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public BigDecimal getTotalIncomeForCurrentUser() {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        return incomeRepository.findTotalIncomeByProfileId(currentUser.getId());
    }


    // --- Helper Methods ---
    private IncomeDTO toDTO(IncomeEntity entity) {
        IncomeDTO dto = new IncomeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category) {
        return IncomeEntity.builder()
                .name(dto.getName())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }
}