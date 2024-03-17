
package com.project.shopapp.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Category;

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
public class CategoryResponse {
	@JsonProperty("message")
	private String message;

	@JsonProperty("category")
	private Category category;
	
	@JsonProperty("errors")
	private List<String> errors;
}
