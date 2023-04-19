package com.example.repository;

import com.example.entity.ArticleTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity, Integer>,
        PagingAndSortingRepository<ArticleTypeEntity, Integer> {

//    @Query(value = "select case ?1 when 'uz' then name_uz " +
//            "when 'ru' then name_ru else name_eng from  article_type", nativeQuery = true)
//    List<>

}
