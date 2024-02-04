package com.alpha.backend.Dto;

import com.alpha.backend.entity.ApplicationUser;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BlogDto {
    private Integer id;
    private String title;
    private List<byte[]> images;
    private String description;
    private Date timeAdded;
    private ApplicationUser user;

}
