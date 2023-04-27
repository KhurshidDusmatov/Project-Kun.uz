package com.example.dto.filter;

import com.example.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ProfileFilterRequestDTO {
//    name,surname,phone,role,created_date_from,created_date_to
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private ProfileRole role;
    private LocalDateTime createdDateFrom;
    private LocalDateTime createdDateTo;
}
