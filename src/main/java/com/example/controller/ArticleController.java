package com.example.controller;

import com.example.dto.article.ArticleDTO;
import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.article.ArticleRequestListDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.entity.ArticleEntity;
import com.example.enums.ArticleStatus;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.service.AttachService;
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
    private final AttachService attachService;

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

    @GetMapping("/type/{id}")
    public ResponseEntity<List<ArticleShortInfoDTO>> getLast8NotGivenList(@RequestBody List<String> list) {
        return ResponseEntity.ok(articleService.getLast8NotGivenList(list));
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
