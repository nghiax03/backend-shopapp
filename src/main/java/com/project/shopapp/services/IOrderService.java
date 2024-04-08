package com.project.shopapp.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;

public interface IOrderService {
	Order createOrder(OrderDTO orderDTO) throws Exception;

	Order getOrder(Long id);

	Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

	void deleteOrder(Long id);

	List<Order> findByUserId(Long userId);

	Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);

}
