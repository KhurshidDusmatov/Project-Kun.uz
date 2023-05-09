package com.example.controller;

import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.dto.savedArticle.SavedArticleRequestDTO;
import com.example.dto.savedArticle.SavedArticleResponseDTO;
import com.example.dto.tag.TagDTO;
import com.example.dto.tag.TagRequestDTO;
import com.example.enums.ProfileRole;
import com.example.service.SavedArticleService;
import com.example.service.TagService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/saved-article")
public class SavedArticleController {

    @Autowired
    private SavedArticleService savedArticleService;

    @PostMapping({"", "/public"})
    public ResponseEntity<SavedArticleRequestDTO> create(@RequestBody SavedArticleRequestDTO dto,
                                         HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(savedArticleService.create(dto, prtId));
    }

    @DeleteMapping("/public/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") String id,
                                          HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(savedArticleService.delete(id, prtId));
    }

    @GetMapping(value = "/public/get-saved-articles")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        List<SavedArticleResponseDTO> list = savedArticleService.getAll(prtId);
        return ResponseEntity.ok(list);
    }
}
