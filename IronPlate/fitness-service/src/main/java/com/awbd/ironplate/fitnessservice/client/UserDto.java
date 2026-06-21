package com.awbd.ironplate.fitnessservice.client;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
}
