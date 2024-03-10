package com.project.shopapp.repositories;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shopapp.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    boolean existsByName(String name);
    
    Page<Product> findAll(Pageable pageable);                 //phan trang cac san pham
}
