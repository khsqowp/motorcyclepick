package com.example.motorcycle.domain;

import com.example.motorcycle.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDomain {
    private String id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String instagram;
    private String role;

    public static UserDomain fromDTO(UserDTO dto) {
        UserDomain domain = new UserDomain();
        domain.setId(dto.getId());
        domain.setUsername(dto.getUsername());
        domain.setPassword(dto.getPassword());
        domain.setEmail(dto.getEmail());
        domain.setPhoneNumber(dto.getPhoneNumber());
        domain.setInstagram(dto.getInstagram());
        domain.setRole(dto.getRole());
        return domain;
    }
}