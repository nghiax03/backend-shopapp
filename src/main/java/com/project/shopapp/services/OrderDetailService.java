package com.project.shopapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
 
	private final OrderDetailRepository orderDetailRepository;
	
	private final OrderRepository orderRepository;
	
	private final ProductRepository productRepository;
	
	@Override
	public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception{
		Order order = orderRepository.findById(orderDetailDTO.getOrderId())
				.orElseThrow(() -> 
				new DataNotFoundException("Cannot find Order with id = " 
				+ orderDetailDTO.getOrderId()));
		Product product = productRepository.findById(orderDetailDTO.getProductId())
				.orElseThrow(() -> new DataNotFoundException("Cannot find Product with id = "
						+ orderDetailDTO.getProductId()));
		OrderDetail orderDetail = OrderDetail.builder()
				.order(order)
				.product(product)
				.numberOfProducts(orderDetailDTO.getNumberOfProducts())
				.price(orderDetailDTO.getPrice())
				.totalMoney(orderDetailDTO.getTotalMoney())
				.color(orderDetailDTO.getColor())
				.build();
		return orderDetailRepository.save(orderDetail);
	}

	@Override
	public OrderDetail getOrderDetail(Long id) throws Exception{
		return orderDetailRepository.findById(id)
				.orElseThrow(()-> new DataNotFoundException("Cannot find OrderDetail with id = " + id));
	}

	@Override
	public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
		OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
				.orElseThrow(
						() -> new DataNotFoundException("Cannot find OrderDetail with id = " + id));
		Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
				.orElseThrow(
						() -> new DataNotFoundException("Cannot find Order with id = " + orderDetailDTO.getOrderId()));
		Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
				.orElseThrow(
						() -> new DataNotFoundException("Cannot find Product with id = " + orderDetailDTO.getProductId()));
		existingOrderDetail.setPrice(orderDetailDTO.getPrice());
		existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
		existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
		existingOrderDetail.setColor(orderDetailDTO.getColor());
		existingOrderDetail.setOrder(existingOrder);
		existingOrderDetail.setProduct(existingProduct);
		return orderDetailRepository.save(existingOrderDetail);
		
	}

	@Override
	public void deleteOrderDetailById(Long id) {
		// TODO Auto-generated method stub
		orderDetailRepository.deleteById(id);
		
	}

	@Override
	public List<OrderDetail> getOrderDetails(Long orderId) {
		return orderDetailRepository.findByOrderId(orderId);
	}

}
