package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDTO {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;
}
