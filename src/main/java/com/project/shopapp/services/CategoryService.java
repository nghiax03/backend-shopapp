package com.project.shopapp.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.repositories.CategoryRepostitory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//thuoc tinh final se duoc tao constructor  tuong ung
public class CategoryService implements ICategoryService {

	private final CategoryRepostitory categoryRepostitory;

	@Override
	@Transactional
	public Category createCategory(CategoryDTO categoryDTO) {
		Category newCategory = Category.builder()
				.name(categoryDTO.getName()).build();
		return categoryRepostitory.save(newCategory);
	}

	@Override
	public Category getCategoryById(Long id) {
		// TODO Auto-generated method stub
		return categoryRepostitory.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return categoryRepostitory.findAll();
	}

	@Override
	@Transactional
	public Category updateCategory(Long categoryId, CategoryDTO categoryDTO) {
		// TODO Auto-generated method stub
		Category existingCategory = getCategoryById(categoryId);
		existingCategory.setName(categoryDTO.getName());
		categoryRepostitory.save(existingCategory);
		return existingCategory;
	}

	@Override
	@Transactional
	public void deleteCategory(Long id) {
		// TODO Auto-generated method stub
		// xoa cung
		categoryRepostitory.deleteById(id);
	}

}
