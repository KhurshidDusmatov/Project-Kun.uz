package com.example.service;

import com.example.dto.article.ArticleDTO;
import com.example.dto.article.ArticleRequestDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ArticleStatus;
import com.example.exps.AppBadRequestException;
import com.example.repository.ArticleRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.RegionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final RegionRepository regionRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final RegionService regionService;
    private final CategoryService categoryService;


    public ArticleRequestDTO create(ArticleRequestDTO dto, Integer moderId) {
        // check
//        ProfileEntity moderator = profileService.get(moderId);
//        RegionEntity region = regionService.get(dto.getRegionId());
//        CategoryEntity category = categoryService.get(dto.getCategoryId());
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setModeratorId(moderId);
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setAttachId(dto.getAttachId());
        // type
        articleRepository.save(entity);
        return dto;
    }


    public Boolean delete(String  id) {
      articleRepository.delete(get(id));
        return true;
    }

    public ArticleEntity get(String id){
        return articleRepository.findById(id).orElseThrow(() -> {
            throw new AppBadRequestException("Article not found");
        });
    }

    //    public ArticleDTO create(ArticleDTO dto) {
//        isValidProfile(dto);
//        ArticleEntity entity = new ArticleEntity();
//        entity.setTitle(dto.getTitle());
//        entity.setDescription(dto.getDescription());
//        entity.setContent(dto.getContent());
//        entity.setSharedCount(dto.getSharedCount());
//
//        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(dto.getRegionId());
//        entity.setRegionId(optionalRegionEntity.get());
//
//        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(dto.getCategoryId());
//        entity.setCategory(optionalCategoryEntity.get());
//        entity.setStatus();(ArticleStatus.NOT_PUBLISHED);
//        articleRepository.save(entity);
//        return dto;
//    }
//    public ArticleDTO update(String id, ArticleDTO dto) {
//        Optional<ArticleEntity> optional = articleRepository.findById(id);
//        if (optional.isEmpty()) {
//            throw new AppBadRequestException(" yoqku ");
//        }
//        // isValidProfile(dto);
//        ArticleEntity entity = optional.get();
////        entity.setId(dto.getId());
//        entity.setTitle(dto.getTitle());
//        entity.setDescription(dto.getDescription());
//        entity.setContent(dto.getContent());
//        entity.setSharedCount(dto.getSharedCount());
//
//        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(dto.getRegionId());
//        entity.setRegion(optionalRegionEntity.get());
//
//        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(dto.getCategoryId());
//        entity.setCategory(optionalCategoryEntity.get());
//        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
//        articleRepository.save(entity);
//
//        dto.setId(entity.getId());
//        return dto;
//    }

//    public boolean update2(String  id, ArticleDTO dto) {
//        Optional<ArticleEntity> optional = articleRepository.findById(id);
//        if (optional.isEmpty()) {
//            throw new AppBadRequestException(" yoqku ");
//        }
//
//        if(dto.getArticleStatus().equals(ArticleStatus.NOT_PUBLISHED)){
//            ArticleEntity entity = optional.get();
//            entity.setStatus(ArticleStatus.PUBLISHED);
//            articleRepository.save(entity);
//        } else if(dto.getArticleStatus().equals(ArticleStatus.PUBLISHED)){
//            ArticleEntity entity = optional.get();
//            entity.setStatus(ArticleStatus.NOT_PUBLISHED);
//            articleRepository.save(entity);
//        }
//        return true;
//    }

//    public Page<ArticleEntity> getAll(int page, int size) {
//        return null;
//    }

//    public void isValidProfile(ArticleDTO dto) {
//        if (dto.getCategoryId() == null) {
//            throw new AppBadRequestException("invalid category");
//        }
//        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(dto.getCategoryId());
//        if (optionalCategoryEntity.isEmpty()) {
//            throw new AppBadRequestException("not found category");
//        }
//
//        if (dto.getRegionId() == null) {
//            throw new AppBadRequestException("invalid region");
//        }
//        Optional<RegionEntity> optionalRegionEntity = regionRepository.findById(dto.getRegionId());
//        if (optionalRegionEntity.isEmpty()) {
//            throw new AppBadRequestException("not found region");
//        }
//        if (dto.getDescription() == null) {
//            throw new AppBadRequestException("invalid desc");
//        }
//        if (dto.getSharedCount() == null) {
//            throw new AppBadRequestException("invalid sharedCount");
//        }
//        if (dto.getContent() == null) {
//            throw new AppBadRequestException("invalid con");
//        }
//        if (dto.getTitle() == null) {
//            throw new AppBadRequestException("invalid title");
//
//        }
//    }

}
