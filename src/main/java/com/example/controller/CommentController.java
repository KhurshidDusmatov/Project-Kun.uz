package com.example.controller;

import com.example.dto.CommentRequestDTO;
import com.example.dto.CommentUpdateRequestDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.dto.region.RegionDTO;
import com.example.dto.savedArticle.SavedArticleRequestDTO;
import com.example.dto.savedArticle.SavedArticleResponseDTO;
import com.example.enums.ProfileRole;
import com.example.service.CommentService;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping({"", "/"})
    public ResponseEntity<CommentRequestDTO> create(@RequestBody CommentRequestDTO dto,
                                         @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN,
                ProfileRole.MODERATOR, ProfileRole.USER,ProfileRole.PUBLISHER );
        return ResponseEntity.ok(commentService.create(dto, jwtDTO.getId()));
    }

//    @PutMapping("/update")
//    public ResponseEntity<String> update(@RequestParam("id") Integer id,
//                                         @RequestBody CommentUpdateRequestDTO dto,
//                                         @RequestHeader("Authorization") String authorization) {
//        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
//        return ResponseEntity.ok(commentService.update(id, dto, jwtDTO.getId(), jwtDTO.getRole()));
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") String id,
                                          @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN,
                ProfileRole.MODERATOR, ProfileRole.USER,ProfileRole.PUBLISHER );
        return ResponseEntity.ok(commentService.delete(id, jwtDTO.getId()));
    }

//    @GetMapping(value = "/get-saved-articles")
//    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authorization) {
//        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN,
//                ProfileRole.MODERATOR, ProfileRole.USER,ProfileRole.PUBLISHER );
//        List<SavedArticleResponseDTO> list = commentService.getAll(jwtDTO.getId());
//        return ResponseEntity.ok(list);
//    }
}
