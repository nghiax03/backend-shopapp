package com.project.shopapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.services.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/categories")
//neu co validated se kiem tra ca class <=> chui vao ham moi hiem tra
//@Validated
//Dependency Injection
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping("")
	// neu tham so truyen vao 1 doi tuong => request object (Data transfer object)
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, //
			BindingResult result) {
		if (result.hasErrors()) {
			// danh sach error
			List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
			return ResponseEntity.badRequest().body(errorMessage);
		}
		categoryService.createCategory(categoryDTO);
		return ResponseEntity.ok("This is create method" + categoryDTO);
	}

	// hien thi tat ca product
	@GetMapping("") // http://localhost:8088/api/v1/categories?page=1&limit=10
	public ResponseEntity<List<Category>> getAllCategories(//
			@RequestParam("page") int page, //
			@RequestParam("limit") int limit) {
		List<Category> categories = categoryService.getAllCategories();
		return ResponseEntity.ok(categories);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateCategory(@PathVariable Long id,//
			@Valid @RequestBody CategoryDTO categoryDTO) {
		categoryService.updateCategory(id,categoryDTO);
		return ResponseEntity.ok("Update category successfully");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.ok("Delete category with id = " + id + " successfully");
	}

}
