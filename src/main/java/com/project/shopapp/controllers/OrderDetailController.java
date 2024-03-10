package com.project.shopapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopapp.dtos.OrderDetailDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
	@PostMapping("")
	private ResponseEntity<?> createOrderDetail() {
		return ResponseEntity.ok("created OrderDetail");

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
		return ResponseEntity.ok("Get OrderDetail with id = " + id);
	}
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
		return ResponseEntity.ok("Get OrderDetail with orderId = " + orderId);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,//
			@RequestBody OrderDetailDTO newOrderDetailDTO){
		return ResponseEntity.ok("Update OrderDetail with id = " + id + 
				", newOrderDetailData " + newOrderDetailDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id){
		return ResponseEntity.noContent().build();
	}
}
