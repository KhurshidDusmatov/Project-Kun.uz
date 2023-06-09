package com.example.controller;

import com.example.dto.article.ArticleTypeDTO;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article-type")

public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping({"/private"})
    public ResponseEntity<ArticleTypeDTO> create(@RequestBody @Valid
                                                 ArticleTypeDTO dto,
                                                 HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ROLE_ADMIN);
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(articleTypeService.create(dto, prtId));
    }

    @PutMapping("/private/update")
    public ResponseEntity<String> update(@RequestParam("id") Integer id,
                                         @RequestBody ArticleTypeDTO dto,
                                         HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ROLE_ADMIN);
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(articleTypeService.update(id, dto, prtId));
    }

    @DeleteMapping("/private/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") Integer id,
                                        HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ROLE_ADMIN);
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(articleTypeService.delete(id, prtId));
    }

    @GetMapping(value = "/public/pagination")
    public ResponseEntity<?> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "4") int size) {
        Page<ArticleTypeDTO> pagination = articleTypeService.pagination(page, size);
        return ResponseEntity.ok(pagination);
    }

    @GetMapping(value = "/public/get-by-language")
    public ResponseEntity<?> getByLanguage(@RequestParam("language") String language) {
        List<ArticleTypeDTO> list = articleTypeService.getByLanguage(language);
        return ResponseEntity.ok(list);
    }


}
