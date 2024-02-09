package com.alpha.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Lob
    @Column(columnDefinition = "bytea")
    private List<byte[]> Image;
    @Column(length = 1000)
    private String description;
    private Date timeAdded;
    @ManyToOne
    @JsonIgnore
    private ApplicationUser user;

}
