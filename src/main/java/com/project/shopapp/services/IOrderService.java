package com.project.shopapp.services;

import java.util.List;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;


public interface IOrderService {
	Order createOrder(OrderDTO orderDTO) throws Exception;

	Order getOrderById(Long id) throws Exception;

	Order updateOrder(Long categoryId, OrderDTO orderDTO) throws Exception;

	void deleteOrder(Long id);
	
	List<Order> findByUserId(Long userId);
}
