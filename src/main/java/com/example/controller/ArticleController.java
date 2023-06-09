package com.example.controller;

import com.example.dto.article.*;
import com.example.enums.ArticleStatus;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping({"/private"})
    public ResponseEntity<?> create(@RequestBody @Valid
                                    ArticleRequestDTO dto,
                                    HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ROLE_MODERATOR);
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(articleService.create(dto, prtId));
    }

    @PutMapping("/private/{id}")
    public ResponseEntity<ArticleRequestDTO> update(@PathVariable("id") String id,
                                                    @RequestBody @Valid ArticleRequestDTO dto,
                                                    HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ROLE_MODERATOR);
        return ResponseEntity.ok(articleService.update(dto, id));
    }

    @DeleteMapping("/private/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id,
                                   HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ROLE_MODERATOR, ProfileRole.ROLE_ADMIN);
        return ResponseEntity.ok(articleService.delete(id));
    }

    @GetMapping("/private/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") String id,
                                          @RequestParam String status,
                                          HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ROLE_PUBLISHER);
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(articleService.changeStatus(ArticleStatus.valueOf(status), id, prtId));
    }

    @GetMapping("/public/type/{id}/five")
    public ResponseEntity<List<ArticleShortInfoDTO>> get5ByTypeId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(articleService.getLast5ByTypeId(id));
    }

    @GetMapping("/public/type/{id}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getNByTypeId(@PathVariable("id") Integer typeId, @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(articleService.getLastNByTypeId(typeId, limit));
    }

    @PostMapping("/public/get-last8")
    public ResponseEntity<List<ArticleShortInfoDTO>> get8ExceptList(@RequestBody ArticleRequestListDTO dto) {
        return ResponseEntity.ok(articleService.getLast8NotGivenList(dto.getIdList()));
    }

    @GetMapping("/private/{id}")
    public ResponseEntity<ArticleShortInfoDTO> getById(@PathVariable("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(currentPrincipalName);
        return ResponseEntity.ok(articleService.getById(id));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ArticleFullInfoDTO> getById(@PathVariable("id") String id,
//                                                      @RequestHeader(value = "Accept-Language", defaultValue = "uz", required = false) String lang) {
//        return ResponseEntity.ok(articleService.getById(id, LangEnum.en));
//    }

    @GetMapping("/public/get-last-4")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast4(@RequestParam("type-id") Integer typeId, @RequestParam("article-id") String articeleId) {
        return ResponseEntity.ok(articleService.getLast4ByType(typeId, articeleId));
    }

    @PostMapping("/public/get-most-read-articles")
    public ResponseEntity<List<ArticleShortInfoDTO>> getMostReadArticles(@RequestParam("type-id") Integer typeId, @RequestParam("article-id") String articeleId) {
        return ResponseEntity.ok(articleService.getMostReadArticles());
    }

    @GetMapping("/public/get-article-by-type-and-region")
    public ResponseEntity<List<ArticleShortInfoDTO>> getArticleByTypeAndRegion(@RequestParam("type-id") Integer typeId, @RequestParam("region-id") Integer regionId) {
        return ResponseEntity.ok(articleService.getArticleByTypeAndRegion(typeId, regionId));
    }

    @GetMapping(value = "/public/pagination-by-region")
    public ResponseEntity<?> paginationByRegion(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "4") int size,
                                                @RequestParam("regionId") Integer regionId) {
        Page<ArticleShortInfoDTO> pagination = articleService.paginationByRegion(page, size, regionId);
        return ResponseEntity.ok(pagination);
    }

    @GetMapping("/public/get-article-by-category")
    public ResponseEntity<List<ArticleShortInfoDTO>> getArticleByCategory(@RequestParam("category-id") Integer categoryId) {
        return ResponseEntity.ok(articleService.getArticleByCategory(categoryId));
    }

    @GetMapping(value = "/public/pagination-by-category")
    public ResponseEntity<?> paginationByCategory(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "4") int size,
                                                  @RequestParam("categoryId") Integer categoryId) {
        Page<ArticleShortInfoDTO> pagination = articleService.paginationByCategory(page, size, categoryId);
        return ResponseEntity.ok(pagination);
    }

    @PostMapping("/public/filter")
    public ResponseEntity<Page<ArticleShortInfoDTO>> filter(@RequestBody ArticleFilterDTO dto,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.filter(dto, page, size));
    }

}
