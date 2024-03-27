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

import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.responses.OrderResponse;
import com.project.shopapp.services.OrderService;
import com.project.shopapp.utils.MessageKeys;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final LocalizationUtils localizationUtils;
	private final OrderService orderService;
	@PostMapping("")
	public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO,//
			BindingResult result) {
		try {
			if(result.hasErrors()) {
				List<String> errorMessage = result.getFieldErrors().stream()
						.map(FieldError::getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errorMessage);
			}
			Order orderResponse = orderService.createOrder(orderDTO);
			return ResponseEntity.ok(orderResponse);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	
	@GetMapping(value = "/user/{user_id}")
	//http://localhost:8088/api/v1/orders/users/1
	public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
		try {
			List<Order> orders = orderService.findByUserId(userId);
			return ResponseEntity.ok(orders);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping(value = "/{id}")
	//http://localhost:8088/api/v1/orders/users/1
	public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId){
		try {
			Order existingOrder =  orderService.getOrderById(orderId);
			return ResponseEntity.ok(existingOrder);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOrder(@Valid @PathVariable("id") Long id,//
			@Valid @RequestBody OrderDTO orderDTO){
		try {
			Order order = orderService.updateOrder(id, orderDTO);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id){
		orderService.deleteOrder(id);
		return ResponseEntity.ok(localizationUtils.getLocalizationUtils(MessageKeys.DELETE_ORDER));
	}
}
