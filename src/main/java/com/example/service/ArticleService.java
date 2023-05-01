package com.example.service;

import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.exps.ItemNotFoundException;
import com.example.mapper.ArticleShortInfoMapper;
import com.example.repository.ArticleRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.RegionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    private final AttachService attachService;
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
        entity.setTypeId(dto.getArticleTypeId());
        entity.setAttachId(dto.getAttachId());
        // type
        articleRepository.save(entity);
        return dto;
    }

    public ArticleRequestDTO update(ArticleRequestDTO dto, String id) {
        ArticleEntity entity = get(id);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setTypeId(dto.getArticleTypeId());
        entity.setAttachId(dto.getAttachId());
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleRepository.save(entity);
        return dto;
    }

    public Boolean delete(String  id) {
      articleRepository.deleteArticle(id);
        return true;
    }

    public ArticleEntity get(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Item not found: " + id);
        }
        return optional.get();
    }


    public Boolean changeStatus(ArticleStatus status, String id, Integer prtId) {
        ArticleEntity entity = get(id);
        if (status.equals(ArticleStatus.PUBLISHED)) {
            entity.setPublishedDate(LocalDateTime.now());
            entity.setPublisherId(prtId);
        }
        entity.setStatus(status);
        articleRepository.save(entity);
        // articleRepository.changeStatus(status, id);
        return true;
    }


    public ArticleShortInfoDTO toArticleShortInfo(ArticleEntity entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setImage(attachService.getAttachLink(entity.getAttachId()));
        return dto;
    }
    public ArticleShortInfoDTO toArticleShortInfo(ArticleShortInfoMapper entity) {
        ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublished_date());
        dto.setImage(attachService.getAttachLink(entity.getAttachId()));
        return dto;
    }

    public List<ArticleShortInfoDTO> getLast5ByTypeId(Integer typeId) {
        List<ArticleShortInfoMapper> entityList = articleRepository.find5ByTypeIdNative(
                typeId,
                ArticleStatus.PUBLISHED.toString(),
                5);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }
    public List<ArticleShortInfoDTO> getLastNByTypeId(Integer typeId, Integer limit) {
        List<ArticleShortInfoMapper> entityList = articleRepository.getTopN(
                typeId,
                ArticleStatus.PUBLISHED.toString(),
                limit);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public List<ArticleShortInfoDTO> getLast8NotGivenList(List<String> list) {
        List<ArticleEntity> all = articleRepository.getAll(ArticleStatus.PUBLISHED);
        List<ArticleShortInfoDTO> result = new ArrayList<>();
        for (ArticleEntity articleEntity : all) {
            for (String id : list) {
                if (!articleEntity.getId().equals(id)){
                    result.add(toArticleShortInfo(articleEntity));
                }
            }
            if (result.size()==8) break;
        }
        return result;
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
