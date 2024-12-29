package com.example.motorcycle.dto;

import com.example.motorcycle.domain.UserDomain;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String instagram;
    private String role;

    // Domain -> DTO 변환
    public static UserDTO fromDomain(UserDomain domain) {
        UserDTO dto = new UserDTO();
        dto.setId(domain.getId());
        dto.setUsername(domain.getUsername());
        dto.setPassword(domain.getPassword());
        dto.setEmail(domain.getEmail());
        dto.setPhoneNumber(domain.getPhoneNumber());
        dto.setInstagram(domain.getInstagram());
        dto.setRole(domain.getRole());
        return dto;
    }

    // DTO -> Domain 변환
    public UserDomain toDomain() {
        UserDomain domain = new UserDomain();
        domain.setId(this.id);
        domain.setUsername(this.username);
        domain.setPassword(this.password);
        domain.setEmail(this.email);
        domain.setPhoneNumber(this.phoneNumber);
        domain.setInstagram(this.instagram);
        domain.setRole(this.role);
        return domain;
    }
}