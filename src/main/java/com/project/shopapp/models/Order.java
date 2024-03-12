package com.project.shopapp.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "orders")
@Entity
@Data // toString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "fullname", length = 100)
	private String fullName;
	
	@Column(name = "email", length = 100)
	private String email;
	
	@Column(name = "phone_number", nullable = false, length = 100)
	private String phoneNumber;
	
	@Column(name = "address", length = 100)
	private String address;
	
	@Column(name = "note", length = 100)
	private String note;
	
	@Column(name = "order_date")
	private Date orderDate;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "total_money")
	private Integer totalMoney;
	
	@Column(name = "shipping_method")
	private String shippingMethod;

	@Column(name = "shipping_address")
	private String shippingAddress;
	
	@Column(name = "shipping_date")
	private LocalDate shippingDate;
	
	@Column(name = "tracking_number")
	private String trackingNumber;
	
	@Column(name = "payment_method")
	private String paymentMethod;
	
	
//	@Column(name = "payment_date")
//	private LocalDateTime paymentDate; //khi ng dung thanh toan moi cap nhat
	
	@Column(name = "active")
	private boolean active; //thuoc ve admin
	
}
