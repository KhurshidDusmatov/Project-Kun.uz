package com.example.controller;

import com.example.dto.ArticleDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.entity.ArticleEntity;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService service;

    @PostMapping({"", "/"})
    public ResponseEntity<?> create(@RequestBody ArticleDTO dto,
                                    @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(service.create(dto));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ArticleDTO> update(@PathVariable("id") String id, @RequestBody ArticleDTO dto,
                                             @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(service.update(id, dto));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") String id,
                                              @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(service.delete(id));

    }

    //    4 chi
    @PutMapping("/update2/{id}")
    public ResponseEntity<Boolean> update2(@PathVariable("id") String id, @RequestBody ArticleDTO dto,
                                           @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(service.update2(id, dto));
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<ArticleEntity>> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "size", defaultValue = "6") int size,
                                                          @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(service.getAll(page, size));
    }

}
