package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {
	@JsonProperty("order_id")
	@Min(value = 1, message = "Order's ID must be > 0")
	private Long orderId;
	
	@JsonProperty("product_id")
	@Min(value = 1, message =  "Product's ID must be > 0")
	private Long productId;
	
	@Min(value = 0, message = "Product's ID must be >= 0")
	private Float price;
	
	@JsonProperty("number_of_products")
	@Min(value = 0, message = "Number_of_Products must be >= 0")
	private int numberOfProducts;
	
	@Min(value = 0, message = "Total money must be >= 0")
	@JsonProperty("total_money")
	private Float totalMoney;
	
	private String color; 
}
