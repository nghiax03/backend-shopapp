package com.project.shopapp.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
	@JsonProperty("fullname")
	private String fullName;
	
	@JsonProperty("phone_number")
	@NotBlank(message = "Phone number is required")
	private String phoneNumber;
	
	private String address;
	
	@NotBlank(message = "Password cannot be blank")
	private String password;
	@JsonProperty("retype_password")
	private String retypePassword;
	
	@JsonProperty("date_of_birth")
	private Date dataOfBirth;
	
	@JsonProperty("facebook_account_id")
	private int facebookAccountId;
	
	@JsonProperty("google_account_id")
	private int googleAccountId;
	
	@JsonProperty("is_active")
	private boolean active;
	
	@NotNull(message = "Role ID is required")
	@JsonProperty("role_id")
	private Long roleId;
	

}
