package com.project.shopapp.services;

import java.util.List;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.models.OrderDetail;

public interface IOrderDetailService {
	OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
	
	OrderDetail getOrderDetail(Long id) throws Exception;
	
	OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;
	
	List<OrderDetail> getOrderDetails(Long orderId);

	void deleteOrderDetailById(Long id);
}
