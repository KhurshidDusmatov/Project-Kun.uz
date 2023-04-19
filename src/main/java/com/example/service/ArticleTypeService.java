package com.example.service;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.entity.ProfileEntity;
import com.example.exps.AppBadRequestException;
import com.example.repository.ArticleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;
    public ArticleTypeDTO create(ArticleTypeDTO dto, Integer adminId) {
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setVisible(true);
        entity.setPrtId(adminId);
        articleTypeRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public String update(Integer id, ArticleTypeDTO dto, Integer adminId) {
        ArticleTypeEntity entity = get(id);
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setPrtId(adminId);
        articleTypeRepository.save(entity);
        return "Successfully updated";
    }

    private ArticleTypeEntity get(Integer id){
        Optional<ArticleTypeEntity> optional = articleTypeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("ArticleType not found");
        }
        return optional.get();
    }

    public String delete(Integer id, Integer adminId) {
        ArticleTypeEntity entity = get(id);
        entity.setVisible(false);
        entity.setPrtId(adminId);
        articleTypeRepository.save(entity);
        return "Profile deleted";
    }

    public Page<ArticleTypeDTO> pagination(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleTypeEntity> entityPage = articleTypeRepository.findAll(pageable);
        List<ArticleTypeEntity> entities = entityPage.getContent();
        List<ArticleTypeDTO> dtos = new LinkedList<>();
        entities.forEach(entity -> {
            ArticleTypeDTO dto = new ArticleTypeDTO();
            dtos.add(toDTO(entity, dto));
        });
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    private ArticleTypeDTO toDTO(ArticleTypeEntity entity, ArticleTypeDTO dto) {
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setNameEn(entity.getNameEn());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<ArticleTypeDTO> getByLanguage(String language) {
        return null;
    }
}
