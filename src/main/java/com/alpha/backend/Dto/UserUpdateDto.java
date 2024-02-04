package com.alpha.backend.Dto;

import com.alpha.backend.entity.Role;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String firstname;
    private String lastname;
    private byte[] Image;
    private String phoneNumber;
}