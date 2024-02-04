package com.alpha.backend.Dto;

import com.alpha.backend.entity.Role;
import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private byte[] Image;
    private String phoneNumber;
    private Role role;
}