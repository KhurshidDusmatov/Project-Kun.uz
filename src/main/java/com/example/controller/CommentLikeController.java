package com.example.controller;

import com.example.dto.jwt.JwtDTO;
import com.example.service.ArticleLikeService;
import com.example.service.CommentLikeService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment-like")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;

    @GetMapping( "/public/like{articleId}")
    public ResponseEntity<?> like(@PathVariable("articleId") Integer commentId,
                                  HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(commentLikeService.like(commentId, prtId));
    }
    @GetMapping( "/public/dislike{commentId}")
    public ResponseEntity<?> dislike(@PathVariable("commentId") Integer commentId,
                                     HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(commentLikeService.dislike(commentId, prtId));
    }
    @GetMapping( "/public/delete{commentId}")
    public ResponseEntity<?> delete(@PathVariable("commentId") Integer commentId,
                                    HttpServletRequest request) {
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(commentLikeService.delete(commentId, prtId));
    }
}
