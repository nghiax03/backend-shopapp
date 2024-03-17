package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.User;

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
public class RegisterResponse {
	@JsonProperty("message")
	private String message;

	@JsonProperty("user")
	private User user;
}
