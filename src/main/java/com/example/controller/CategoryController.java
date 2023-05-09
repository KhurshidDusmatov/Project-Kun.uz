package com.example.controller;

import com.example.dto.category.CategoryDTO;
import com.example.dto.jwt.JwtDTO;
import com.example.dto.region.RegionDTO;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")

public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping({ "/private"})
    public ResponseEntity<CategoryDTO> create(@RequestBody @Valid
                                              CategoryDTO dto,
                                              HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(categoryService.create(dto, prtId));
    }
    @PutMapping("/private/update")
    public ResponseEntity<String> update(@RequestParam("id") Integer id,
                                         @RequestBody CategoryDTO dto,
                                        HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(categoryService.update(id, dto, prtId));
    }
    @DeleteMapping("/private/delete")
    public ResponseEntity<Boolean> delete(@RequestParam("id") Integer id,
                                          HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        Integer prtId = (Integer) request.getAttribute("id");
        return ResponseEntity.ok(categoryService.delete(id, prtId));
    }
    @GetMapping(value = "/public/get-all")
    public ResponseEntity<?> getAll() {
        List<CategoryDTO> list = categoryService.getAll();
        return ResponseEntity.ok(list);
    }

//    @GetMapping(value = "/get-by-language")
////    public ResponseEntity<?> getByLanguage(@RequestParam("language") String language) {
////        List<CategoryDTO> list = categoryService.getByLanguage(language);
////        return ResponseEntity.ok(list);
////    }


}
