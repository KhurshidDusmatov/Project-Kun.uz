package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleTypeDTO {
    private Integer id;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private LocalDateTime createdDate;

}
