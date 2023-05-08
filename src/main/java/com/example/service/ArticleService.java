package com.example.service;

import com.example.dto.article.ArticleFilterDTO;
import com.example.dto.article.ArticleFullInfoDTO;
import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.enums.LangEnum;
import com.example.exps.AppBadRequestException;
import com.example.exps.ItemNotFoundException;
import com.example.mapper.ArticleShortInfoMapper;
import com.example.repository.ArticleCustomRepository;
import com.example.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final AttachService attachService;
    private final ArticleTypeService articleTypeService;
    private final RegionService regionService;
    private final CategoryService categoryService;
    private final ArticleCustomRepository articleCustomRepository;


    public ArticleRequestDTO create(ArticleRequestDTO dto, Integer moderId) {
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setModeratorId(moderId);
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setTypeId(dto.getArticleTypeId());
        entity.setAttachId(dto.getAttachId());
        articleRepository.save(entity);
        dto.setId(entity.getId());
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

    public Boolean delete(String id) {
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
        List<ArticleEntity> all = articleRepository.get8ByExceptList(ArticleStatus.PUBLISHED, list);
        List<ArticleShortInfoDTO> result = new ArrayList<>();
        all.forEach(entity -> {
            result.add(toArticleShortInfo(entity));
        });
        return result;
    }

    public ArticleFullInfoDTO getById(String id, LangEnum langEnum) {
        ArticleEntity entity = get(id);
        if (!entity.getVisible() || !entity.getStatus().equals(ArticleStatus.PUBLISHED)) {
            throw new AppBadRequestException("Wrong article status");
        }
        return toFullDTO(entity, langEnum);
    }

    public ArticleFullInfoDTO toFullDTO(ArticleEntity entity, LangEnum langEnum) {
        ArticleFullInfoDTO dto = new ArticleFullInfoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setViewCount(entity.getViewCount());
        dto.setImage(attachService.getAttachLink(entity.getAttachId()));
        dto.setCategory(categoryService.getByIdAndLang(entity.getCategoryId(), langEnum));
        dto.setRegion(regionService.getByIdAndLang(entity.getCategoryId(), langEnum));
        dto.setArticleType(articleTypeService.getByIdAndLang(entity.getCategoryId(), langEnum));
        // tag_list
        return dto;
    }

    public List<ArticleShortInfoDTO> getLast4ByType(Integer typeId, String articleId) {
        List<ArticleShortInfoDTO> last5ByTypeId = getLast5ByTypeId(typeId);
        List<ArticleShortInfoDTO> resultList = new ArrayList<>();
        for (ArticleShortInfoDTO item : last5ByTypeId) {
            if (!articleId.equals(item.getId())) {
                resultList.add(item);
            }
        }
        return resultList;
    }


    public List<ArticleShortInfoDTO> getMostReadArticles() {
        List<ArticleEntity> entityList = articleRepository.get4ByMostRead(
                ArticleStatus.PUBLISHED);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public List<ArticleShortInfoDTO> getArticleByTypeAndRegion(Integer typeId, Integer regionId) {
        List<ArticleEntity> all = articleRepository.getAllByTypeAndRegion(
                typeId,
                regionId,
                ArticleStatus.PUBLISHED);

        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        all.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public Page<ArticleShortInfoDTO> paginationByRegion(int page, int size, Integer regionId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleEntity> entityPage = articleRepository.findAllByRegionId(regionId, pageable);
        List<ArticleEntity> entities = entityPage.getContent();
        List<ArticleShortInfoDTO> dtos = new LinkedList<>();
        entities.forEach(entity -> {
            dtos.add(toArticleShortInfo(entity));
        });
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    public List<ArticleShortInfoDTO> getArticleByCategory(Integer categoryId) {
        List<ArticleEntity> all = articleRepository.getAllByCategory(
                categoryId,
                ArticleStatus.PUBLISHED);

        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        all.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return dtoList;
    }

    public Page<ArticleShortInfoDTO> paginationByCategory(int page, int size, Integer categoryId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleEntity> entityPage = articleRepository.findAllByCategoryId(categoryId, pageable);
        List<ArticleEntity> entities = entityPage.getContent();
        List<ArticleShortInfoDTO> dtos = new LinkedList<>();
        entities.forEach(entity -> {
            dtos.add(toArticleShortInfo(entity));
        });
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    public PageImpl<ArticleShortInfoDTO> filter(ArticleFilterDTO filterDTO, int page, int size) {
        Page<ArticleEntity> pageObj = articleCustomRepository.filter(filterDTO, page, size);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        pageObj.forEach(entity -> {
            dtoList.add(toArticleShortInfo(entity));
        });
        return new PageImpl<>(dtoList, PageRequest.of(page, size), pageObj.getTotalElements());
    }

}
