package com.project.shopapp.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UpdateUserDTO {
	@JsonProperty("fullname")
	private String fullName;
	
	@JsonProperty("phone_number")
	private String phoneNumber;
	
	private String address;
	
	private String password;

	@JsonProperty("date_of_birth")
	private Date dataOfBirth;
	
	@JsonProperty("facebook_account_id")
	private int facebookAccountId;
	
	@JsonProperty("google_account_id")
	private int googleAccountId;
	
	@JsonProperty("is_active")
	private boolean active;
}
