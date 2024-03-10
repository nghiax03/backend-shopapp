package com.project.shopapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopapp.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	// tim don hang cua 1 user
	List<Order> findByUserId(Long userId);

}
