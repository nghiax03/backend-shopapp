package com.project.shopapp.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponse  extends BaseResponse {
	private Long id;
	private String name;	
	private Float price;
	private String thumbnail;
	private String description;
	
	@JsonProperty("product_images")
	private List<ProductImage> productImages = new ArrayList<>();

	@JsonProperty("category_id")
	private Long categoryId;
	
	public static ProductResponse formProduct(Product product) {
		ProductResponse productResponse =  ProductResponse.builder()
				  .id(product.getId())
	              .name(product.getName())
	              .price(product.getPrice())
	              .thumbnail(product.getThumbnail())
	              .description(product.getDescription())
	              .categoryId(product.getCategory().getId())
	              .productImages(product.getProductImages())
	              .build();
				  productResponse.setCreatedAt(product.getCreatedAt());
				  productResponse.setUpdatedAt(product.getUpdatedAt());
				  return productResponse;
				  
	}
}
