package com.example.dto;

import com.example.kun_uz_.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDTO {
    private String Id;
    private String title;
    private String description;
    private String content;
    private Integer sharedCount;
    private Integer imageId;
    private Integer regionId;
    private Integer categoryId;
    private Integer moderatorId;
    private Integer publisherId;
    private Integer viewCount;
    private ArticleStatus articleStatus;

}
