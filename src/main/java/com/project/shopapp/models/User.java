package com.project.shopapp.models;

import java.util.Date;

import jakarta.annotation.Generated;
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

@Table(name = "users")
@Entity
@Data // toString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "fullname", length = 100)
	private String fullName;
	
	@Column(name = "phone_number", length = 20, nullable = false)
	private String phoneNumber;
	
	@Column(name = "address" ,length = 200)
	private String address;
	
	@Column(name = "password", length = 100, nullable = false)
	private String password;
	
	@Column(name = "is_active")
	private boolean active;
	
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@Column(name = "facebook_account_id")
	private int facebookAccountId;
	
	@Column(name = "google_account_id")
	private int googleAccountId;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private com.project.shopapp.models.Role role;
	
}
