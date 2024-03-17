package com.project.shopapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.responses.CategoryResponse;
import com.project.shopapp.responses.UpdateCategoryResponse;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.utils.MessageKeys;

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
    private final LocalizationUtils localizationUtils;

	@PostMapping("")
	// neu tham so truyen vao 1 doi tuong => request object (Data transfer object)
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, //
			BindingResult result) {
		CategoryResponse categoryResponse = new CategoryResponse();
		if (result.hasErrors()) {
			// danh sach error
			List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
			categoryResponse.setMessage(localizationUtils
					.getLocalizationUtils(MessageKeys.CREATE_CATEGORY_FAILED));
			categoryResponse.setErrors(errorMessage);
			return ResponseEntity.badRequest().body(categoryResponse);
		}
		Category category = categoryService.createCategory(categoryDTO);
		categoryResponse.setCategory(category);
		return ResponseEntity.ok(categoryResponse);
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
	public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable Long id,//
			@Valid @RequestBody CategoryDTO categoryDTO) {
		UpdateCategoryResponse upCategoryResponse = new UpdateCategoryResponse();
		categoryService.updateCategory(id,categoryDTO);
		upCategoryResponse.setMessage(localizationUtils
				.getLocalizationUtils(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY));
		return ResponseEntity.ok(upCategoryResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.ok(localizationUtils.getLocalizationUtils(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));
	}

}
