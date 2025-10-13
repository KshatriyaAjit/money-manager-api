package com.money.manager.money_manager_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String type;
    private Long profileId; // To link back to the user
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
