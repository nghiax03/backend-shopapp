package com.project.shopapp.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.responses.ProductResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper redisObjectMapper;

	private String getKeyFrom(String keyword, Long categoryId, PageRequest pageRequest) {
		int pageNumber = pageRequest.getPageNumber();
		int pageSize = pageRequest.getPageSize();
		Sort sort = pageRequest.getSort();
		String sortDirection = sort.getOrderFor("id").getDirection() 
				== Sort.Direction.ASC ? "asc" : "desc";
		String key = String
				.format("all_products:%d:%d:%s", pageNumber, pageSize, sortDirection);

		return key;
	}

	@Override
	public List<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest)
			throws JsonProcessingException {
		String key = this.getKeyFrom(keyword, categoryId, pageRequest);
		String json = (String)redisTemplate.opsForValue().get(key);
		List<ProductResponse> productResponses = 
				json != null ? redisObjectMapper.readValue(json,
						new TypeReference<List<ProductResponse>>(){}) : null;
		return productResponses;
	}

	@Override
	public void clear() {
		redisTemplate.getConnectionFactory().getConnection().flushAll();

	}

	@Override
	public void saveAllProducts(List<ProductResponse> productResponses, String keyword, Long categoryId,
			PageRequest pageRequest) throws JsonProcessingException {
		String key = this.getKeyFrom(keyword, categoryId, pageRequest);
		String json = redisObjectMapper.writeValueAsString(productResponses);
		redisTemplate.opsForValue().set(key, json);
	}
}
