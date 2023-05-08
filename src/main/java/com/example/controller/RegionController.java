package com.example.controller;

import com.example.dto.article.ArticleRequestDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.dto.region.RegionDTO;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/region")

public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping({ "/private"})
    public ResponseEntity<RegionDTO> create(@RequestBody @Valid
                                            RegionDTO dto,
                                            HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(regionService.create(dto, prtId));
    }

    @PutMapping("/private/update")
    public ResponseEntity<String> update(@RequestParam("id") Integer id,
                                         @RequestBody RegionDTO dto,
                                         @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.update(id, dto, jwtDTO.getId()));
    }

    @DeleteMapping("/private/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") Integer id,
                                          @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.delete(id, jwtDTO.getId()));
    }

    @GetMapping(value = "/public/get-all")
    public ResponseEntity<?> getAll() {
        List<RegionDTO> list = regionService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/public/get-by-language")
    public ResponseEntity<?> getByLanguage(@RequestParam("language") String language) {
        List<RegionDTO> list = regionService.getByLanguage(language);
        return ResponseEntity.ok(list);
    }


}
