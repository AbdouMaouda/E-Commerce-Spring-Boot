package com.ecommerce.Project.service;

import com.ecommerce.Project.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {

List<Category> getCategories();

void createCategory(Category category);

String DeleteCategory(Long categoryId);

Category updateCategory(Category category,Long categoryId);

}
