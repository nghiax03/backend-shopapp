package com.project.shopapp.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.project.shopapp.dtos.CartItemDTO;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
	
	private final OrderRepository orderRepository;
	
	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;
	
	private final ProductRepository productRepository;
	
	private final OrderDetailRepository orderDetailRepository;
	
	@Override
	public Order createOrder(OrderDTO orderDTO) throws Exception {
		//tim xem userId co ton tai khong
	    User user = userRepository.findById(orderDTO.getUserId())
	    		.orElseThrow(() -> 
	    		new DataNotFoundException("Cannot find user with id = " + orderDTO.getUserId()));
        //convert orderDTO => Order
	    //new Thu vien ModelMapper
	    modelMapper.typeMap(OrderDTO.class, Order.class)
	    .addMappings(mapper->mapper.skip(Order::setId)); 
	  
	    Order order = new Order();
	    modelMapper.map(orderDTO, order);
	    order.setUser(user);
	    order.setOrderDate(new Date());
	    order.setStatus(OrderStatus.PENDING);
	    //shipping >= ngay hom qua
	    LocalDate shippingDate = orderDTO.getShippingDate()
	    		== null ? LocalDate.now() : orderDTO.getShippingDate();
	    if(shippingDate.isBefore(LocalDate.now())) {
	    	throw new DataNotFoundException("Date must be least today!");
	    }
	    order.setShippingDate(shippingDate);
	    order.setActive(true);
	    order.setTotalMoney(orderDTO.getTotalMoney());
	    orderRepository.save(order);
	    List<OrderDetail> orderDetails = new ArrayList<>();
	    for(CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
	    	//tao 1 doi tuong orderdetail tu cartitemDTO
	    	OrderDetail orderDetail = new OrderDetail();
	    	orderDetail.setOrder(order);
	    	
	    	Long productId = cartItemDTO.getProductId();
	    	int quantity = cartItemDTO.getQuantity();
	    	
	    	//tim tt san pham tuu co so du lieu
	    	Product product = productRepository.findById(productId)
	    			.orElseThrow(() -> new DataNotFoundException("Product not found with id: "
	    					  + productId));
	    	
	    	orderDetail.setProduct(product);
	    	orderDetail.setNumberOfProducts(quantity);
	    	
	    	orderDetail.setPrice(product.getPrice());
	    	orderDetails.add(orderDetail);
	    }
	    orderDetailRepository.saveAll(orderDetails);
	    return order;
	}

	@Override
	public Order getOrderById(Long id) throws Exception{
		Order order =  orderRepository.findById(id)
				.orElseThrow(()-> new DataNotFoundException("Cannot find by id: " + id));
		return order;
	}

	@Override
	public Order updateOrder(Long id, OrderDTO orderDTO) throws Exception{
		Order order = orderRepository.findById(id)
				.orElseThrow(()
						-> new DataNotFoundException("Cannot find order with id = " + id));
		User existingUser = userRepository.findById(orderDTO.getUserId())
				.orElseThrow(()
						-> new DataNotFoundException("Cannot find User with user_id = " + id));
		//anh xa
		modelMapper.typeMap(OrderDTO.class, Order.class)
		           .addMappings(mapping -> mapping.skip(Order::setId));
		modelMapper.map(orderDTO, order);
		order.setUser(existingUser);
		order.setUser(existingUser);
		return orderRepository.save(order);
	}

	@Override
	public void deleteOrder(Long id) {
		Order order = orderRepository.findById(id).orElse(null);
		//xoa mem
		if(order != null) {
			order.setActive(false);
			orderRepository.save(order);
		}

	}

	@Override
	public List<Order> findByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}

}
