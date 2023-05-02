package com.example.service;

import com.example.dto.CommentRequestDTO;
import com.example.dto.savedArticle.SavedArticleRequestDTO;
import com.example.dto.savedArticle.SavedArticleResponseDTO;
import com.example.entity.CommentEntity;
import com.example.entity.SavedArticleEntity;
import com.example.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public CommentRequestDTO create(CommentRequestDTO dto, Integer ownerId) {
        CommentEntity entity = new CommentEntity();
        entity.setArticleId(dto.getArticleId());
        entity.setOwnerId(ownerId);
        entity.setReplyId(dto.getReplyId());
        entity.setCreatedDate(LocalDateTime.now());
        commentRepository.save(entity);
        return dto;
    }
    public Boolean delete(String articleId, Integer ownerId) {
        savedArticleRepository.deleteSavedArticle(articleId, ownerId);
        return true;
    }

    public List<SavedArticleResponseDTO> getAll(Integer ownerId) {
        List<SavedArticleEntity> all = savedArticleRepository.getAll(ownerId);
        List<SavedArticleResponseDTO> dtos = new ArrayList<>();
        all.forEach(savedArticleEntity -> {
            dtos.add(toDTO(savedArticleEntity));
        });
        return dtos;
    }
}
