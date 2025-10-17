package com.ecommerce.Project.controller;

import com.ecommerce.Project.model.Category;
import com.ecommerce.Project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getCategories() {
 List<Category> categories = categoryService.getCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> CreateCategories( @Valid @RequestBody
                                                   Category category) {

        categoryService.createCategory(category);

        return new ResponseEntity<>("Category created successfully", HttpStatus.CREATED);

    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        try {
            String status = categoryService.DeleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category,
                                                 @PathVariable Long categoryId) {
try{
    Category savedCategory=categoryService.updateCategory(category,categoryId);

    return new ResponseEntity<>("Category with id: "+savedCategory.getCategoryId(), HttpStatus.OK);
}catch(ResponseStatusException e){
    return new ResponseEntity<>(e.getReason(), e.getStatusCode());
}
    }

}
