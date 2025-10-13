package com.money.manager.money_manager_api.dto;

import lombok.Data;

@Data
public class AuthDTO {
    private String email;
    private String password;
}