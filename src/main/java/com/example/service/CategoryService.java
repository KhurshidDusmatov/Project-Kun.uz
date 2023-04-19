package com.example.service;


import com.example.dto.CategoryDTO;
import com.example.entity.CategoryEntity;
import com.example.exps.AppBadRequestException;
import com.example.repository.CategoryRepository;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto, Integer adminId) {
        CategoryEntity entity = new CategoryEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setVisible(true);
        entity.setPrtId(adminId);
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public String update(Integer id, CategoryDTO dto, Integer adminId) {
        CategoryEntity entity = get(id);
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setPrtId(adminId);
        categoryRepository.save(entity);
        return "Successfully updated";
    }

    private CategoryEntity get(Integer id){
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadRequestException("ArticleType not found");
        }
        return optional.get();
    }

    public String delete(Integer id, Integer adminId) {
        CategoryEntity entity = get(id);
        entity.setVisible(false);
        entity.setPrtId(adminId);
        categoryRepository.save(entity);
        return "Profile deleted";
    }

//    public Page<CategoryDTO> pagination(Integer page, Integer size) {
//        Sort sort = Sort.by(Sort.Direction.ASC, "id");
//        Pageable pageable = PageRequest.of(page - 1, size, sort);
//        Page<CategoryEntity> entityPage = categoryRepository.findAll(pageable);
//        List<CategoryEntity> entities = entityPage.getContent();
//        List<CategoryDTO> dtos = new LinkedList<>();
//        entities.forEach(entity -> {
//            CategoryDTO dto = new CategoryDTO();
//            dtos.add(toDTO(entity, dto));
//        });
//        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
//    }

    private CategoryDTO toDTO(CategoryEntity entity, CategoryDTO dto) {
        dto.setId(entity.getId());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setNameEn(entity.getNameEn());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<CategoryDTO> getByLanguage(String language) {
        return null;
    }

    public List<CategoryDTO> getAll() {
        Iterable<CategoryEntity> iterable = categoryRepository.findAll();
        List<CategoryDTO> dtos = new LinkedList<>();
        iterable.forEach(regionEntity -> {
            CategoryDTO dto = new CategoryDTO();
            dtos.add(toDTO(regionEntity, dto));
        });
        return dtos;
    }
}
