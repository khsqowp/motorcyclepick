package com.example.motorcyclepick.dto;

import com.example.motorcyclepick.domain.UserDomain;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;  // 이것을 사용


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
    private LocalDate birthDate;
    private String region;
    private String encryptedEmail;
    private String encryptedPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


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
        dto.setBirthDate(domain.getBirthDate());
        dto.setRegion(domain.getRegion());
        dto.setEncryptedEmail(domain.getEncryptedEmail());
        dto.setEncryptedPhone(domain.getEncryptedPhone());
        dto.setCreatedAt(domain.getCreatedAt());
        dto.setUpdatedAt(domain.getUpdatedAt());
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
        domain.setBirthDate(this.birthDate);
        domain.setRegion(this.region);
        domain.setEncryptedEmail(this.encryptedEmail);
        domain.setEncryptedPhone(this.encryptedPhone);
        domain.setCreatedAt(this.createdAt);
        domain.setUpdatedAt(this.updatedAt);
        return domain;
    }
}