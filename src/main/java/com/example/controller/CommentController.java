package com.example.controller;

import com.example.dto.article.ArticleFilterDTO;
import com.example.dto.article.ArticleShortInfoDTO;
import com.example.dto.comment.CommentFilterDTO;
import com.example.dto.comment.CommentRequestDTO;
import com.example.dto.comment.CommentResponseDTO;
import com.example.dto.comment.CommentUpdateRequestDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.dto.profile.ProfileDTO;
import com.example.enums.ProfileRole;
import com.example.service.CommentService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping({"/public"})
    public ResponseEntity<CommentRequestDTO> create(@RequestBody CommentRequestDTO dto,
                                                    HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(commentService.create(dto, prtId));
    }

    @PutMapping("/public/update")
    public ResponseEntity<Boolean> update(@RequestParam("id") Integer id,
                                          @RequestBody CommentUpdateRequestDTO dto,
                                          HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(commentService.update(id, prtId, dto));
    }

    @DeleteMapping("/public/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") Integer id,
                                          HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        ProfileRole role = (ProfileRole) request.getAttribute("role");
        return ResponseEntity.ok(commentService.delete(id, prtId, role));
    }

    @GetMapping("/public/get-all/{articleId}")
    public ResponseEntity<?> getAll(@PathVariable String articleId) {
        return ResponseEntity.ok(commentService.getListByArticleId(articleId));
    }

    @GetMapping(value = "/private/pagination")
    public ResponseEntity<?> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "4") int size,
                                        HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        Page<CommentResponseDTO> pagination = commentService.pagination(page, size);
        return ResponseEntity.ok(pagination);
    }

    @PostMapping("/private/filter")
    public ResponseEntity<Page<CommentResponseDTO>> filter(@RequestBody CommentFilterDTO dto,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(commentService.filter(dto, page, size));
    }

    @GetMapping("public/get-all-by-comment/{commentId}")
    public ResponseEntity<?> getAllByCommentId(@PathVariable Integer commentId) {
        return ResponseEntity.ok(commentService.getListByCommentId(commentId));
    }

}
