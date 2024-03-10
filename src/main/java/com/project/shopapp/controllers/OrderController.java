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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopapp.dtos.OrderDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
	@PostMapping("")
	public ResponseEntity<?> orderCreader(@RequestBody @Valid OrderDTO orderDTO,//
			BindingResult result) {
		try {
			if(result.hasErrors()) {
				List<String> errorMessage = result.getFieldErrors().stream()
						.map(FieldError::getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errorMessage);
			}
			return ResponseEntity.ok("createOrder successfully");
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	
	@GetMapping(value = "/{user_id}")
	public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
		try {
			return ResponseEntity.ok("Lay ra danh sach order tu user_id");
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrder(@Valid @PathVariable long id,//
			@Valid @RequestBody OrderDTO orderDTO){
		return ResponseEntity.ok("cap nhat thong tin 1 order");
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id){
		return ResponseEntity.ok("Order deleted successfully");
	}
}
