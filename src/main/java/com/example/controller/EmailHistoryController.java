package com.example.controller;

import com.example.dto.EmailHistoryDTO;
import com.example.enums.ProfileRole;
import com.example.service.EmailHistoryService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/email-history")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;

    @GetMapping(value = "/private/get-by-email")
    public ResponseEntity<?> getAll(@RequestParam("email") String email,
                                    HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        List<EmailHistoryDTO> list = emailHistoryService.getByEmail(email);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/private/get-by-created-date")
    public ResponseEntity<?> getAll(@RequestParam("date") LocalDateTime dateTime,
                                    HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        List<EmailHistoryDTO> list = emailHistoryService.getByCreatedDate(dateTime);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/private/pagination")
    public ResponseEntity<?> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "4") int size,
                                        HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        Page<EmailHistoryDTO> pagination = emailHistoryService.pagination(page, size);
        return ResponseEntity.ok(pagination);
    }

}
