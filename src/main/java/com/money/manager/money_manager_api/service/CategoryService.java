package com.money.manager.money_manager_api.service;

import com.money.manager.money_manager_api.dto.CategoryDTO;
import com.money.manager.money_manager_api.entity.CategoryEntity;
import com.money.manager.money_manager_api.entity.ProfileEntity;
import com.money.manager.money_manager_api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        CategoryEntity categoryEntity = toEntity(categoryDTO, currentUser);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        return toDTO(savedCategory);
    }

    public List<CategoryDTO> getCategoriesForCurrentUser() {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(currentUser.getId());
        return categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all categories of a specific type for the currently authenticated user.
     * @param type The category type ("INCOME" or "EXPENSE").
     * @return A list of the user's categories of that type.
     */
    public List<CategoryDTO> getCategoriesForCurrentUserAndType(String type) {
        ProfileEntity currentUser = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileIdAndType(currentUser.getId(), type);

        return categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Helper Methods for Conversion ---

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        if (entity.getProfile() != null) {
            dto.setProfileId(entity.getProfile().getId());
        }
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private CategoryEntity toEntity(CategoryDTO dto, ProfileEntity profile) {
        return CategoryEntity.builder()
                .name(dto.getName())
                .type(dto.getType())
                .profile(profile)
                .build();
    }
}