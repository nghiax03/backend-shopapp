package com.project.shopapp.responses;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
   private Long id;
   
   @JsonProperty("user_id")
   private Long userId;
   
   @JsonProperty("fullname")
   private String fullname;
   
   @JsonProperty("phone_number")
   private String phoneNumber;
   
   @JsonProperty("email")
   private String email;
    
   @JsonProperty("address")
   private String address;
   
   @JsonProperty("note")
   private String note;
   
   @JsonProperty("order_date")
   private LocalDate orderDate;
   
   @JsonProperty("status")
   private String status;
   
   @JsonProperty("total_money")
   private double totalMoney;
   
   @JsonProperty("shipping_method")
   private String shippingMethod;
   
   @JsonProperty("shipping_address")
   private String shippingAddress;
   
   @JsonProperty("shipping_date")
   private LocalDate shippingDate;
   
   @JsonProperty("payment_method")
   private String paymentMethod;
   
   @JsonProperty("order_detail")
   private List<OrderDetail> orderDetails;
   
   public static OrderResponse formOrder(Order order) {
	   OrderResponse orderResponse = OrderResponse
			   .builder()
			   .id(order.getId())
			   .userId(order.getId())
			   .fullname(order.getFullName())
			   .phoneNumber(order.getPhoneNumber())
			   .email(order.getEmail())
			   .address(order.getAddress())
			   .note(order.getNote())
               .orderDate(order.getOrderDate())
               .status(order.getStatus())
               .totalMoney(order.getTotalMoney())
               .shippingMethod(order.getShippingMethod())
               .shippingAddress(order.getShippingAddress())
               .shippingDate(order.getShippingDate())
               .paymentMethod(order.getPaymentMethod())
               .orderDetails(order.getOrderDetails())
               .build();
       return orderResponse;

   }
   
   
}
