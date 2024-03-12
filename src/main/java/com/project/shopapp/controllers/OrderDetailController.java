package com.project.shopapp.controllers;

import java.util.List;

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
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.OrderDetailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
	
	private final OrderDetailService orderDetailService;
	
	@PostMapping("")
	private ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
		try {
			OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
			return ResponseEntity.ok(OrderDetailResponse.formDetail(orderDetail));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
		 try {
		OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
			return ResponseEntity.ok(orderDetail);
			                   //.ok(OrderDetailResponse.formDetail(orderDetail));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());	
		}
	}
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(orderId);
		List<OrderDetailResponse> orderDetailResponses = orderDetails
				.stream()
				.map(OrderDetailResponse::formDetail).toList();
		return ResponseEntity.ok(orderDetailResponses);
		                   //.ok(orderDetails);
		                   
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,//
			@RequestBody OrderDetailDTO OrderDetailDTO){
		try {
			OrderDetail orderDetail = orderDetailService.updateOrderDetail(id,OrderDetailDTO);
			return ResponseEntity.ok(orderDetail);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id){
		orderDetailService.deleteOrderDetailById(id);
		return ResponseEntity.ok().body("Deleted OrderDetail with id = " + id + " successfully!");
	}
}
