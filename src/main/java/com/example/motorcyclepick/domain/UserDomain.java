package com.example.motorcyclepick.domain;

import com.example.motorcyclepick.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;  // 이것을 사용



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
    private LocalDate birthDate;
    private String region;
    private String encryptedEmail;
    private String encryptedPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static UserDomain fromDTO(UserDTO dto) {
        UserDomain domain = new UserDomain();
        domain.setId(dto.getId());
        domain.setUsername(dto.getUsername());
        domain.setPassword(dto.getPassword());
        domain.setEmail(dto.getEmail());
        domain.setPhoneNumber(dto.getPhoneNumber());
        domain.setInstagram(dto.getInstagram());
        domain.setRole(dto.getRole());
        domain.setBirthDate(dto.getBirthDate());
        domain.setRegion(dto.getRegion());
        domain.setEncryptedEmail(dto.getEncryptedEmail());
        domain.setEncryptedPhone(dto.getEncryptedPhone());
        domain.setCreatedAt(dto.getCreatedAt());
        domain.setUpdatedAt(dto.getUpdatedAt());
        return domain;
    }

    public UserDTO toDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(this.id);
        dto.setUsername(this.username);
        dto.setPassword(this.password);
        dto.setEmail(this.email);
        dto.setPhoneNumber(this.phoneNumber);
        dto.setInstagram(this.instagram);
        dto.setRole(this.role);
        dto.setBirthDate(this.birthDate);
        dto.setRegion(this.region);
        dto.setEncryptedEmail(this.encryptedEmail);
        dto.setEncryptedPhone(this.encryptedPhone);
        dto.setCreatedAt(this.createdAt);
        dto.setUpdatedAt(this.updatedAt);
        return dto;
    }
}