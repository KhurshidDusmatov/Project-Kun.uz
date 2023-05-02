package com.example.controller;

import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.article.ArticleRequestListDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.enums.ArticleStatus;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping({"", "/"})
    public ResponseEntity<?> create(@RequestBody @Valid ArticleRequestDTO dto,
                                    @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto, jwtDTO.getId()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ArticleRequestDTO> update(@PathVariable("id") String id,
                                                    @RequestBody @Valid ArticleRequestDTO dto,
                                                    @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id,
                                    @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PostMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") String id,
                                          @RequestParam String status,
                                          @RequestHeader("Authorization") String authorization) {
        JwtDTO jwt = JwtUtil.getJwtDTO(authorization, ProfileRole.PUBLISHER);
        return ResponseEntity.ok(articleService.changeStatus(ArticleStatus.valueOf(status), id, jwt.getId()));
    }
    @GetMapping("/type/{id}/five")
    public ResponseEntity<List<ArticleShortInfoDTO>> get5ByTypeId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(articleService.getLast5ByTypeId(id));
    }

    @GetMapping("/type/{id}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getNByTypeId(@PathVariable("id") Integer typeId, @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(articleService.getLastNByTypeId(typeId, limit));
    }

    @GetMapping("/get-last8")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast8NotGivenList(@RequestBody ArticleRequestListDTO dto) {
        return ResponseEntity.ok(articleService.getLast8NotGivenList(dto.getIdList()));
    }
    @GetMapping("/get-by-id-lang")
    public ResponseEntity<?> getByIdAndLang(@RequestParam("id") String id, @RequestParam(
            "language") String language) {
        return ResponseEntity.ok(articleService.getByIdAndLanguage(id, language));
    }

    @GetMapping("/get-last-4")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast4(@RequestParam("type-id") Integer typeId, @RequestParam("article-id") String articeleId) {
        return ResponseEntity.ok(articleService.getLast4ByType(typeId, articeleId));
    }

    @GetMapping("/get-most-read-articles")
    public ResponseEntity<List<ArticleShortInfoDTO>> getMostReadArticles(@RequestParam("type-id") Integer typeId, @RequestParam("article-id") String articeleId) {
        return ResponseEntity.ok(articleService.getMostReadArticles());
    }

    @GetMapping("/get-article-by-type-and-region")
    public ResponseEntity<List<ArticleShortInfoDTO>> getArticleByTypeAndRegion(@RequestParam("type-id") Integer typeId, @RequestParam("region-id") Integer regionId) {
        return ResponseEntity.ok(articleService.getArticleByTypeAndRegion(typeId, regionId));
    }

    @GetMapping(value = "/pagination-by-region")
    public ResponseEntity<?> paginationByRegion(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "4") int size,
                                                @RequestParam("regionId") Integer regionId) {
        Page<ArticleShortInfoDTO> pagination = articleService.paginationByRegion(page, size, regionId);
        return ResponseEntity.ok(pagination);
    }

    @GetMapping("/get-article-by-category")
    public ResponseEntity<List<ArticleShortInfoDTO>> getArticleByCategory(@RequestParam("category-id") Integer categoryId) {
        return ResponseEntity.ok(articleService.getArticleByCategory(categoryId));
    }
    @GetMapping(value = "/pagination-by-category")
    public ResponseEntity<?> paginationByCategory(@RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "size", defaultValue = "4") int size,
                                                 @RequestParam("categoryId") Integer categoryId) {
        Page<ArticleShortInfoDTO> pagination = articleService.paginationByCategory(page, size, categoryId);
        return ResponseEntity.ok(pagination);
    }


    //    4 chi
//    @PutMapping("/update2/{id}")
//    public ResponseEntity<Boolean> update2(@PathVariable("id") String id, @RequestBody ArticleDTO dto,
//                                           @RequestHeader("Authorization") String authorization) {
//        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
//        return ResponseEntity.ok(service.update2(id, dto));
//    }


//    @GetMapping("/pagination")
//    public ResponseEntity<Page<ArticleEntity>> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
//                                                          @RequestParam(value = "size", defaultValue = "6") int size,
//                                                          @RequestHeader("Authorization") String authorization) {
//        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
//        return ResponseEntity.ok(service.getAll(page, size));
//    }

}
