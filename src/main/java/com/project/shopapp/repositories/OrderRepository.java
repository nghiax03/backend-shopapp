package com.project.shopapp.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.shopapp.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	// tim don hang cua 1 user
	List<Order> findByUserId(Long userId);
	
	@Query("SELECT o FROM Order o WHERE " + 
	       "(:keyword IS NULL OR :keyword = '' OR o.fullName LIKE %:keyword% OR o.address LIKE %:keyword% " +
			"OR o.note LIKE %:keyword%)")
	Page<Order> findByKeyword(String keyword, Pageable pageable);
}
