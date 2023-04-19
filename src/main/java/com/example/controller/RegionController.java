package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.JwtDTO;
import com.example.dto.RegionDTO;
import com.example.enums.ProfileRole;
import com.example.exps.MethodNotAllowedException;
import com.example.service.ArticleTypeService;
import com.example.service.RegionService;
import com.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/region")

public class RegionController {
    @Autowired
    private RegionService regionService;
    @PostMapping({"", "/"})
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO dto,
                                                 @RequestHeader("Authorization") String authorization) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRole().equals(ProfileRole.ADMIN)) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        return ResponseEntity.ok(regionService.create(dto, jwtDTO.getId()));
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam("id") Integer id,
                                             @RequestBody RegionDTO dto,
                                             @RequestHeader("Authorization") String authorization) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRole().equals(ProfileRole.ADMIN)) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        return ResponseEntity.ok(regionService.update(id, dto, jwtDTO.getId()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("id") Integer id,
                                         @RequestHeader("Authorization") String authorization) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRole().equals(ProfileRole.ADMIN)) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        return ResponseEntity.ok(regionService.delete(id, jwtDTO.getId()));
    }

    @GetMapping(value = "/pagination")
    public ResponseEntity<?> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "4") int size) {
        Page<RegionDTO> pagination = regionService.pagination(page, size);
        return ResponseEntity.ok(pagination);
    }

    @GetMapping(value = "/get-by-language")
    public ResponseEntity<?> getByLanguage(@RequestParam("language") String language) {
        List<RegionDTO> list = regionService.getByLanguage(language);
        return ResponseEntity.ok(list);
    }


}
