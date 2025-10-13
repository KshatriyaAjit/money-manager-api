package com.money.manager.money_manager_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProfileDTO {
    private Long id;
    private String fullName;
    private String email;
    private String password; // Only used for sending data TO the server
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
}