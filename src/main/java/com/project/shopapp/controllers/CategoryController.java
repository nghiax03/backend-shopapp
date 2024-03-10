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

import jakarta.validation.Valid;


@RestController
@RequestMapping("${api.prefix}/categories")
//neu co validated se kiem tra ca class <=> chui vao ham moi hiem tra
//@Validated
public class CategoryController {
//hien thi tat ca product
	@GetMapping("") //http://localhost:8088/api/v1/categories?page=1&limit=10
	public ResponseEntity<String> getAllCategories(//
			@RequestParam("page") int page, //
			@RequestParam("limit") int limit) {
		return ResponseEntity.ok(String.format("getAllCategories, page = %d, limit = %d", page, limit));
	}

	@PostMapping("")
	//neu tham so truyen vao 1 doi tuong => request object (Data transfer object)
	public ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO,
			BindingResult result) {
		if(result.hasErrors()) {
			//danh sach error
			List<String> errorMessage =  result.getFieldErrors()
				   .stream()
			       .map(FieldError::getDefaultMessage)
			       .toList();
			return ResponseEntity.badRequest().body(errorMessage);
		}
		return ResponseEntity.ok("This is create method" + categoryDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateCategory(@PathVariable Long id) {
		return ResponseEntity.ok("this is put method, update category with id = " + id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
		return ResponseEntity.ok("this is delete method, delete category with id = " + id);
	}

}
