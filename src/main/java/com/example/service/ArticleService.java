package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.RegionEntity;
import com.example.exps.AppBadRequestException;
import com.example.repository.ArticleRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.example.kun_uz_.enums.ArticleStatus;

import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ArticleDTO create(ArticleDTO dto) {
        isValidProfile(dto);
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setShared_count(dto.getSharedCount());

        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(dto.getRegionId());
        entity.setRegion_id(optionalRegionEntity.get());

        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(dto.getCategoryId());
        entity.setCategory(optionalCategoryEntity.get());
        entity.setArticleStatus(ArticleStatus.NOTPUBLISHED);
        articleRepository.save(entity);
        return dto;
    }

    public void isValidProfile(ArticleDTO dto) {
        if (dto.getCategoryId() == null) {
            throw new AppBadRequestException("invalid category");
        }
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(dto.getCategoryId());
        if (optionalCategoryEntity.isEmpty()) {
            throw new AppBadRequestException("not found category");
        }

        if (dto.getRegionId() == null) {
            throw new AppBadRequestException("invalid region");
        }
        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(dto.getRegionId());
        if (optionalRegionEntity.isEmpty()) {
            throw new AppBadRequestException("not found region");
        }
        if (dto.getDescription() == null) {
            throw new AppBadRequestException("invalid desc");
        }
        if (dto.getSharedCount() == null) {
            throw new AppBadRequestException("invalid sharedCount");
        }
        if (dto.getContent() == null) {
            throw new AppBadRequestException("invalid con");
        }
        if (dto.getTitle() == null) {
            throw new AppBadRequestException("invalid title");

        }
    }

    public ArticleDTO update(String id, ArticleDTO dto) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException(" yoqku ");
        }
       // isValidProfile(dto);
        ArticleEntity entity = optional.get();
//        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setShared_count(dto.getSharedCount());

        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(dto.getRegionId());
        entity.setRegion_id(optionalRegionEntity.get());

        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(dto.getCategoryId());
        entity.setCategory(optionalCategoryEntity.get());
        entity.setArticleStatus(ArticleStatus.NOTPUBLISHED);
        articleRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    public Boolean delete(String  id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if(optional.isEmpty()){
            throw new AppBadRequestException("yo'qku");
        }
      articleRepository.delete(optional.get());
        return true;
    }

    public boolean update2(String  id, ArticleDTO dto) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException(" yoqku ");
        }

        if(dto.getArticleStatus().equals(ArticleStatus.NOTPUBLISHED)){
            ArticleEntity entity = optional.get();
            entity.setArticleStatus(ArticleStatus.PUBLISHED);
            articleRepository.save(entity);
        } else if(dto.getArticleStatus().equals(ArticleStatus.PUBLISHED)){
            ArticleEntity entity = optional.get();
            entity.setArticleStatus(ArticleStatus.NOTPUBLISHED);
            articleRepository.save(entity);
        }
        return true;
    }

    public Page<ArticleEntity> getAll(int page, int size) {

        return null;
    }



  /*  public Article get(Integer id) {
        return articleRepository.findById(id).orElseThrow(() -> {
            throw new AppBadRequestException("Profile not found");
        });
    }*/
}
