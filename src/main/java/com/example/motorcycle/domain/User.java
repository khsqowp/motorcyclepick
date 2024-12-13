package com.example.motorcycle.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String instagram;

    private String role;
}