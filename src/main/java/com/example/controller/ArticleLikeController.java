package com.example.controller;

import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.service.ArticleLikeService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article-like")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @GetMapping("/public/like{articleId}")
    public ResponseEntity<?> like(@PathVariable("articleId") String articleId,
                                  HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(articleLikeService.like(articleId, prtId));
    }

    @GetMapping("/public/dislike{articleId}")
    public ResponseEntity<?> dislike(@PathVariable("articleId") String articleId,
                                     HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(articleLikeService.dislike(articleId, prtId));
    }

    @GetMapping("/public/delete{articleId}")
    public ResponseEntity<?> delete(@PathVariable("articleId") String articleId,
                                    HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(articleLikeService.delete(articleId, prtId));
    }
}
