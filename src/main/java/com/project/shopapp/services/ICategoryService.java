package com.project.shopapp.services;

import java.util.List;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;

public interface ICategoryService {
	Category createCategory(CategoryDTO category);
	
    Category getCategoryById(long id);
    
    List<Category> getAllCategories();
    
    Category updateCategory(long categoryId, CategoryDTO category);
    
    void deleteCategory(long id);

}
