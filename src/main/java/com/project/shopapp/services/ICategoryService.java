package com.project.shopapp.services;

import java.util.List;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;

public interface ICategoryService {
	Category createCategory(CategoryDTO category);
	
	Category getCategoryById(Long id);
	
	List<Category> getAllCategories();
	
	Category updateCategory(Long categoryId, CategoryDTO categoryDTO);
	
	void deleteCategory(Long id);
}
