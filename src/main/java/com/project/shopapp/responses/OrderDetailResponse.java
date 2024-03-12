package com.project.shopapp.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponse {

	private Long id;

	@JsonProperty("order_id")
	private Long orderId;

	@JsonProperty("product_id")
	private Long productId;

	@JsonProperty("price")
	private Float price;
	@JsonProperty("number_of_products")
	private int numberOfProducts;

	@JsonProperty("total_money")
	private Float totalMoney;

	private String color;
	
	public static OrderDetailResponse formDetail(OrderDetail orderDetail) {
		return   OrderDetailResponse
				.builder()
				.id(orderDetail.getId())
				.orderId(orderDetail.getOrder().getId())
				.productId(orderDetail.getProduct().getId())
				.price(orderDetail.getPrice())
				.numberOfProducts(orderDetail.getNumberOfProducts())
				.totalMoney(orderDetail.getTotalMoney())
				.color(orderDetail.getColor())
				.build();
	}

}
